using DevriyeTakip.API.Models;

namespace DevriyeTakip.API.Data
{
    // Uygulama ilk açıldığında veritabanı boşsa temel rolleri ve
    // bir test kullanıcısını oluşturur. Böylece klonlayıp çalıştıran herkes
    // hazır bir hesapla giriş yapabilir.
    public static class DbSeeder
    {
        public static void Seed(AppDbContext context)
        {
            // 1. Roller
            if (!context.Roles.Any())
            {
                context.Roles.AddRange(
                    new Role { RoleName = "Saha_Guvenlik" },
                    new Role { RoleName = "Yonetici" }
                );
                context.SaveChanges();
            }

            // 2. Test kullanıcısı (Sicil: 12345 / Şifre: password123)
            if (!context.Users.Any())
            {
                var sahaRol = context.Roles.First(r => r.RoleName == "Saha_Guvenlik");

                context.Users.Add(new User
                {
                    FirstName = "Test",
                    LastName = "Personel",
                    RegistrationNumber = "12345",
                    // Şifre asla düz metin tutulmaz; BCrypt ile hash'lenir
                    PasswordHash = BCrypt.Net.BCrypt.HashPassword("password123"),
                    RoleId = sahaRol.Id,
                    IsActive = true
                });
                context.SaveChanges();
            }
        }
    }
}
