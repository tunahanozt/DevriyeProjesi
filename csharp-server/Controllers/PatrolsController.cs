using DevriyeTakip.API.Dtos;
using DevriyeTakip.API.Services;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace DevriyeTakip.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [Authorize]
    public class PatrolsController : ControllerBase
    {
        private readonly IPatrolService _patrolService;

        public PatrolsController(IPatrolService patrolService) => _patrolService = patrolService;

        // Yönetici: bir personele rota atar (yeni devriye oluşturur)
        [HttpPost("assign")]
        public async Task<ActionResult<PatrolDto>> Assign([FromBody] AssignPatrolDto dto)
        {
            var patrol = await _patrolService.AssignAsync(dto);
            return patrol is null
                ? NotFound(new { mesaj = "Personel veya rota bulunamadı." })
                : Ok(patrol);
        }

        // Personelin devriyelerini getirir
        [HttpGet("user/{userId}")]
        public async Task<ActionResult<IEnumerable<PatrolDto>>> GetByUser(int userId)
            => Ok(await _patrolService.GetByUserAsync(userId));

        // Personel: devriyeyi başlatır
        [HttpPost("{id}/start")]
        public async Task<ActionResult<PatrolDto>> Start(int id)
        {
            var patrol = await _patrolService.StartAsync(id);
            return patrol is null ? NotFound() : Ok(patrol);
        }

        // Personel: devriyeyi tamamlar
        [HttpPost("{id}/complete")]
        public async Task<ActionResult<PatrolDto>> Complete(int id)
        {
            var patrol = await _patrolService.CompleteAsync(id);
            return patrol is null ? NotFound() : Ok(patrol);
        }
    }
}
