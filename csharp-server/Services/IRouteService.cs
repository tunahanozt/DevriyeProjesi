using DevriyeTakip.API.Dtos;

namespace DevriyeTakip.API.Services
{
    // Rota (devriye güzergâhı) iş kuralları.
    public interface IRouteService
    {
        Task<RouteDto> CreateRouteAsync(CreateRouteDto dto);
        Task<RouteDto?> AddPointAsync(int routeId, AddRoutePointDto dto);
        Task<IEnumerable<RouteDto>> GetAllAsync();
        Task<RouteDto?> GetByIdAsync(int id);
    }
}
