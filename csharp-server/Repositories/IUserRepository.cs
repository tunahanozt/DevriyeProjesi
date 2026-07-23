using DevriyeTakip.API.Models;

namespace DevriyeTakip.API.Repositories
{
    public interface IUserRepository
    {
        // Sicil numarasına (RegistrationNumber) göre kullanıcıyı, rolüyle birlikte getirir
        Task<User?> GetBySicilNoAsync(string sicilNo);
    }
}
