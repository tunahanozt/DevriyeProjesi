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

        // (Diğer User, Route, Shift sınıflarını yazdıkça buraya ekleyeceğiz)
    }
}