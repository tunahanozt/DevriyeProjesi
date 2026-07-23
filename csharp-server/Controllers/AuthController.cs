using DevriyeTakip.API.Models;
using DevriyeTakip.API.Repositories;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace DevriyeTakip.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class AuthController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        private readonly IUserRepository _userRepository;

        public AuthController(IConfiguration configuration, IUserRepository userRepository)
        {
            _configuration = configuration;
            _userRepository = userRepository;
        }

        // Basit bir DTO (Data Transfer Object) modeli
        public class LoginRequest
        {
            public string SicilNo { get; set; } = string.Empty;
            public string Sifre { get; set; } = string.Empty;
        }

        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] LoginRequest request)
        {
            // Sicil numarasına göre kullanıcıyı veritabanından bul
            var user = await _userRepository.GetBySicilNoAsync(request.SicilNo);

            // Kullanıcı yoksa veya pasif durumdaysa girişi reddet
            if (user is null || !user.IsActive)
            {
                return Unauthorized(new { mesaj = "Hatalı sicil numarası veya şifre." });
            }

            // Gelen düz şifreyi, veritabanındaki hash ile güvenli şekilde karşılaştır
            bool sifreDogruMu = BCrypt.Net.BCrypt.Verify(request.Sifre, user.PasswordHash);
            if (!sifreDogruMu)
            {
                return Unauthorized(new { mesaj = "Hatalı sicil numarası veya şifre." });
            }

            var token = GenerateJwtToken(user);
            return Ok(new { token });
        }

        private string GenerateJwtToken(User user)
        {
            var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["Jwt:Key"]!));
            var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);

            var claims = new List<Claim>
            {
                // Sub: token'ın sahibi olan kullanıcının kimliği (Id)
                new Claim(JwtRegisteredClaimNames.Sub, user.Id.ToString()),
                new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString()),
                new Claim("sicilNo", user.RegistrationNumber),
                // Rol bazlı yetkilendirme için (örn: [Authorize(Roles = "Yonetici")])
                new Claim(ClaimTypes.Role, user.Role?.RoleName ?? "Bilinmiyor")
            };

            var token = new JwtSecurityToken(
                issuer: _configuration["Jwt:Issuer"],
                audience: _configuration["Jwt:Audience"],
                claims: claims,
                expires: DateTime.UtcNow.AddHours(8), // Token geçerlilik süresi (8 saatlik vardiya)
                signingCredentials: credentials);

            return new JwtSecurityTokenHandler().WriteToken(token);
        }
    }
}
