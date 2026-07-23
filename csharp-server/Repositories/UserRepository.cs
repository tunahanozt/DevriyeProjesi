using DevriyeTakip.API.Data;
using DevriyeTakip.API.Models;
using Microsoft.EntityFrameworkCore;

namespace DevriyeTakip.API.Repositories
{
    // IUserRepository kontratını uygular
    public class UserRepository : IUserRepository
    {
        private readonly AppDbContext _context;

        public UserRepository(AppDbContext context)
        {
            _context = context;
        }

        public async Task<User?> GetBySicilNoAsync(string sicilNo)
        {
            return await _context.Users
                .Include(u => u.Role) // JWT'de rol adını kullanabilmek için rolü de çekiyoruz
                .FirstOrDefaultAsync(u => u.RegistrationNumber == sicilNo);
        }
    }
}
