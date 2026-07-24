using DevriyeTakip.API.Data;
using DevriyeTakip.API.Dtos;
using DevriyeTakip.API.Models;
using Microsoft.EntityFrameworkCore;

namespace DevriyeTakip.API.Services
{
    public class RouteService : IRouteService
    {
        private readonly AppDbContext _context;

        public RouteService(AppDbContext context) => _context = context;

        public async Task<RouteDto> CreateRouteAsync(CreateRouteDto dto)
        {
            var route = new PatrolRoute { Name = dto.Name, Description = dto.Description };
            _context.PatrolRoutes.Add(route);
            await _context.SaveChangesAsync();
            return ToDto(route);
        }

        public async Task<RouteDto?> AddPointAsync(int routeId, AddRoutePointDto dto)
        {
            var route = await _context.PatrolRoutes.FirstOrDefaultAsync(r => r.Id == routeId);
            if (route is null) return null;

            // Eklenmek istenen kontrol noktası gerçekten var mı?
            var checkpointExists = await _context.Checkpoints.AnyAsync(c => c.Id == dto.CheckpointId);
            if (!checkpointExists) return null;

            _context.RoutePoints.Add(new RoutePoint
            {
                PatrolRouteId = routeId,
                CheckpointId = dto.CheckpointId,
                Order = dto.Order
            });
            await _context.SaveChangesAsync();

            // Güncel haliyle (noktalarıyla birlikte) döndür
            return await GetByIdAsync(routeId);
        }

        public async Task<IEnumerable<RouteDto>> GetAllAsync()
        {
            var routes = await _context.PatrolRoutes
                .Include(r => r.Points).ThenInclude(p => p.Checkpoint)
                .ToListAsync();
            return routes.Select(ToDto);
        }

        public async Task<RouteDto?> GetByIdAsync(int id)
        {
            var route = await _context.PatrolRoutes
                .Include(r => r.Points).ThenInclude(p => p.Checkpoint)
                .FirstOrDefaultAsync(r => r.Id == id);
            return route is null ? null : ToDto(route);
        }

        private static RouteDto ToDto(PatrolRoute r) => new()
        {
            Id = r.Id,
            Name = r.Name,
            Description = r.Description,
            IsActive = r.IsActive,
            Points = r.Points
                .OrderBy(p => p.Order)
                .Select(p => new RoutePointDto
                {
                    Id = p.Id,
                    CheckpointId = p.CheckpointId,
                    CheckpointName = p.Checkpoint?.Name ?? string.Empty,
                    Order = p.Order
                })
                .ToList()
        };
    }
}
