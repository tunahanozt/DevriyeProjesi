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

        public AuthController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        // Basit bir DTO (Data Transfer Object) modeli
        public class LoginRequest
        {
            public string SicilNo { get; set; } = string.Empty;
            public string Sifre { get; set; } = string.Empty;
        }

        [HttpPost("login")]
        public IActionResult Login([FromBody] LoginRequest request)
        {
            // İlerleyen aşamalarda burada veritabanına bağlanıp kullanıcının şifresini kontrol edeceğiz.
            // Şimdilik sistemin çalıştığını görmek için statik bir kontrol yapıyoruz.
            if (request.SicilNo == "12345" && request.Sifre == "password123")
            {
                var token = GenerateJwtToken(request.SicilNo);
                return Ok(new { token = token });
            }

            return Unauthorized(new { mesaj = "Hatalı sicil numarası veya şifre." });
        }

        private string GenerateJwtToken(string sicilNo)
        {
            var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_configuration["Jwt:Key"]!));
            var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);

            var claims = new[]
            {
                new Claim(JwtRegisteredClaimNames.Sub, sicilNo),
                new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString()),
                new Claim(ClaimTypes.Role, "Saha_Guvenlik") // İleride yetki bazlı işlemler için
            };

            var token = new JwtSecurityToken(
                issuer: _configuration["Jwt:Issuer"],
                audience: _configuration["Jwt:Audience"],
                claims: claims,
                expires: DateTime.Now.AddHours(8), // Token'ın geçerlilik süresi (Örn: 8 Saatlik Vardiya)
                signingCredentials: credentials);

            return new JwtSecurityTokenHandler().WriteToken(token);
        }
    }
}