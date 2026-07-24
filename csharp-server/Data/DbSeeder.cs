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

            // 3. Örnek bölge
            if (!context.Zones.Any())
            {
                context.Zones.Add(new Zone { ZoneName = "Ana Yerleşke" });
                context.SaveChanges();
            }

            // 4. Örnek kontrol noktaları (GPS'li)
            if (!context.Checkpoints.Any())
            {
                var zone = context.Zones.First();
                context.Checkpoints.AddRange(
                    new Checkpoint { Name = "Güvenlik Kulübesi", NfcUID = "04A1B2C3", ZoneId = zone.Id, Latitude = 41.0082, Longitude = 28.9784, Description = "Başlangıç noktası" },
                    new Checkpoint { Name = "A Blok Girişi",     NfcUID = "04D4E5F6", ZoneId = zone.Id, Latitude = 41.0090, Longitude = 28.9790 },
                    new Checkpoint { Name = "Kazan Dairesi",     NfcUID = "04A7B8C9", ZoneId = zone.Id, Latitude = 41.0075, Longitude = 28.9800 }
                );
                context.SaveChanges();
            }

            // 5. Örnek rota (sıralı noktalarla) + test personeline atama
            if (!context.PatrolRoutes.Any())
            {
                var noktalar = context.Checkpoints.OrderBy(c => c.Id).ToList();

                var rota = new PatrolRoute
                {
                    Name = "Standart Gece Turu",
                    Description = "Kulübe -> A Blok -> Kazan Dairesi",
                    Points = noktalar
                        .Select((c, i) => new RoutePoint { CheckpointId = c.Id, Order = i + 1 })
                        .ToList()
                };
                context.PatrolRoutes.Add(rota);
                context.SaveChanges();

                var personel = context.Users.First(u => u.RegistrationNumber == "12345");
                context.Patrols.Add(new Patrol
                {
                    UserId = personel.Id,
                    PatrolRouteId = rota.Id,
                    Status = PatrolStatus.Atandi
                });
                context.SaveChanges();
            }
        }
    }
}
