using DevriyeTakip.API.Models;
using Microsoft.EntityFrameworkCore;

namespace DevriyeTakip.API.Data
{
    public class AppDbContext : DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options)
        {
        }

        // C# sınıflarımızı SQL tablolarına dönüştürecek olan DbSet tanımlamaları
        public DbSet<Checkpoint> Checkpoints { get; set; }
        public DbSet<Incident> Incidents { get; set; }
        public DbSet<User> Users { get; set; }
        public DbSet<Role> Roles { get; set; }
        public DbSet<Zone> Zones { get; set; }

        // Faz 1 - Çekirdek devriye domaini
        public DbSet<PatrolRoute> PatrolRoutes { get; set; }
        public DbSet<RoutePoint> RoutePoints { get; set; }
        public DbSet<Patrol> Patrols { get; set; }
        public DbSet<PatrolScan> PatrolScans { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            // SQL Server "çoklu cascade yolu" hatasını önlemek için:
            // Checkpoint ve User'a giden ilişkilerde silme davranışını Restrict yapıyoruz.
            // (Bir noktayı/kullanıcıyı silmek, ona bağlı rota/devriye kayıtlarını otomatik silmemeli.)

            modelBuilder.Entity<RoutePoint>()
                .HasOne(rp => rp.PatrolRoute)
                .WithMany(r => r.Points)
                .HasForeignKey(rp => rp.PatrolRouteId)
                .OnDelete(DeleteBehavior.Cascade); // Rota silinince noktaları da gider

            modelBuilder.Entity<RoutePoint>()
                .HasOne(rp => rp.Checkpoint)
                .WithMany()
                .HasForeignKey(rp => rp.CheckpointId)
                .OnDelete(DeleteBehavior.Restrict);

            modelBuilder.Entity<Patrol>()
                .HasOne(p => p.User)
                .WithMany()
                .HasForeignKey(p => p.UserId)
                .OnDelete(DeleteBehavior.Restrict);

            modelBuilder.Entity<Patrol>()
                .HasOne(p => p.PatrolRoute)
                .WithMany()
                .HasForeignKey(p => p.PatrolRouteId)
                .OnDelete(DeleteBehavior.Restrict);

            modelBuilder.Entity<PatrolScan>()
                .HasOne(s => s.Patrol)
                .WithMany(p => p.Scans)
                .HasForeignKey(s => s.PatrolId)
                .OnDelete(DeleteBehavior.Cascade); // Devriye silinince okumaları da gider

            modelBuilder.Entity<PatrolScan>()
                .HasOne(s => s.Checkpoint)
                .WithMany()
                .HasForeignKey(s => s.CheckpointId)
                .OnDelete(DeleteBehavior.Restrict);
        }
    }
}
