using DevriyeTakip.API.Dtos;
using DevriyeTakip.API.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace DevriyeTakip.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [Authorize]
    public class RoutesController : ControllerBase
    {
        private readonly IRouteService _routeService;

        public RoutesController(IRouteService routeService) => _routeService = routeService;

        [HttpGet]
        public async Task<ActionResult<IEnumerable<RouteDto>>> GetAll()
            => Ok(await _routeService.GetAllAsync());

        [HttpGet("{id}")]
        public async Task<ActionResult<RouteDto>> GetById(int id)
        {
            var route = await _routeService.GetByIdAsync(id);
            return route is null ? NotFound() : Ok(route);
        }

        // Yönetici: yeni rota oluşturur
        [HttpPost]
        public async Task<ActionResult<RouteDto>> Create([FromBody] CreateRouteDto dto)
        {
            var route = await _routeService.CreateRouteAsync(dto);
            return CreatedAtAction(nameof(GetById), new { id = route.Id }, route);
        }

        // Yönetici: rotaya sıralı bir nokta ekler
        [HttpPost("{id}/points")]
        public async Task<ActionResult<RouteDto>> AddPoint(int id, [FromBody] AddRoutePointDto dto)
        {
            var route = await _routeService.AddPointAsync(id, dto);
            return route is null
                ? NotFound(new { mesaj = "Rota veya kontrol noktası bulunamadı." })
                : Ok(route);
        }
    }
}
