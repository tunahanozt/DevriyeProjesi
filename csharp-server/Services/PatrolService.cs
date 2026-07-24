using DevriyeTakip.API.Data;
using DevriyeTakip.API.Dtos;
using DevriyeTakip.API.Models;
using Microsoft.EntityFrameworkCore;

namespace DevriyeTakip.API.Services
{
    public class PatrolService : IPatrolService
    {
        private readonly AppDbContext _context;

        public PatrolService(AppDbContext context) => _context = context;

        public async Task<PatrolDto?> AssignAsync(AssignPatrolDto dto)
        {
            var userExists = await _context.Users.AnyAsync(u => u.Id == dto.UserId);
            var routeExists = await _context.PatrolRoutes.AnyAsync(r => r.Id == dto.PatrolRouteId);
            if (!userExists || !routeExists) return null;

            var patrol = new Patrol
            {
                UserId = dto.UserId,
                PatrolRouteId = dto.PatrolRouteId,
                Status = PatrolStatus.Atandi
            };
            _context.Patrols.Add(patrol);
            await _context.SaveChangesAsync();
            return await LoadDtoAsync(patrol.Id);
        }

        public async Task<IEnumerable<PatrolDto>> GetByUserAsync(int userId)
        {
            var patrols = await _context.Patrols
                .Include(p => p.User)
                .Include(p => p.PatrolRoute)
                .Where(p => p.UserId == userId)
                .OrderByDescending(p => p.AssignedAt)
                .ToListAsync();
            return patrols.Select(ToDto);
        }

        public async Task<PatrolDto?> StartAsync(int patrolId)
        {
            var patrol = await _context.Patrols.FindAsync(patrolId);
            if (patrol is null) return null;

            patrol.Status = PatrolStatus.Basladi;
            patrol.StartedAt = DateTime.UtcNow;
            await _context.SaveChangesAsync();
            return await LoadDtoAsync(patrolId);
        }

        public async Task<PatrolDto?> CompleteAsync(int patrolId)
        {
            var patrol = await _context.Patrols.FindAsync(patrolId);
            if (patrol is null) return null;

            patrol.Status = PatrolStatus.Tamamlandi;
            patrol.CompletedAt = DateTime.UtcNow;
            await _context.SaveChangesAsync();
            return await LoadDtoAsync(patrolId);
        }

        private async Task<PatrolDto?> LoadDtoAsync(int patrolId)
        {
            var patrol = await _context.Patrols
                .Include(p => p.User)
                .Include(p => p.PatrolRoute)
                .FirstOrDefaultAsync(p => p.Id == patrolId);
            return patrol is null ? null : ToDto(patrol);
        }

        private static PatrolDto ToDto(Patrol p) => new()
        {
            Id = p.Id,
            UserId = p.UserId,
            PersonelAdi = p.User is null ? string.Empty : $"{p.User.FirstName} {p.User.LastName}",
            PatrolRouteId = p.PatrolRouteId,
            RotaAdi = p.PatrolRoute?.Name ?? string.Empty,
            Status = p.Status.ToString(),
            AssignedAt = p.AssignedAt,
            StartedAt = p.StartedAt,
            CompletedAt = p.CompletedAt
        };
    }
}
