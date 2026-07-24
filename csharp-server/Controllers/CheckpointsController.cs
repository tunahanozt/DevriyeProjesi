using DevriyeTakip.API.Dtos;
using DevriyeTakip.API.Hubs;
using DevriyeTakip.API.Models;
using DevriyeTakip.API.Repositories;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.SignalR;

namespace DevriyeTakip.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [Authorize]
    public class CheckpointsController : ControllerBase
    {
        private readonly ICheckpointRepository _repository;
        private readonly IHubContext<LiveTrackingHub> _hubContext; // Canlı takip için sinyal merkezi

        public CheckpointsController(ICheckpointRepository repository, IHubContext<LiveTrackingHub> hubContext)
        {
            _repository = repository;
            _hubContext = hubContext;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<CheckpointDto>>> GetAllCheckpoints()
        {
            var noktalar = await _repository.GetAllAsync();
            var dtolar = noktalar.Select(ToDto);
            return Ok(dtolar);
        }

        // Yönetici: yeni kontrol noktası (GPS'li) tanımlar
        [HttpPost]
        public async Task<ActionResult<CheckpointDto>> Create([FromBody] CreateCheckpointDto dto)
        {
            var checkpoint = new Checkpoint
            {
                Name = dto.Name,
                NfcUID = dto.NfcUID,
                ZoneId = dto.ZoneId,
                Latitude = dto.Latitude,
                Longitude = dto.Longitude,
                Description = dto.Description,
                IsActive = true
            };

            var created = await _repository.AddAsync(checkpoint);
            return Ok(ToDto(created));
        }

        [HttpPost("scan")]
        public async Task<IActionResult> ScanCheckpoint([FromBody] string nfcUid)
        {
            var checkpoint = await _repository.GetActiveByUidAsync(nfcUid);

            if (checkpoint == null)
            {
                return NotFound(new { mesaj = "Bu NFC etiketine tanımlı aktif nokta bulunamadı." });
            }

            await _hubContext.Clients.All.SendAsync("ReceiveScanEvent", new
            {
                NoktaAdi = checkpoint.Name,
                OkumaZamani = DateTime.UtcNow,
                Mesaj = "Nokta kontrol edildi!"
            });

            return Ok(new { mesaj = "Nokta başarıyla okutuldu ve merkeze iletildi.", noktaAdi = checkpoint.Name });
        }

        private static CheckpointDto ToDto(Checkpoint c) => new()
        {
            Id = c.Id,
            Name = c.Name,
            NfcUID = c.NfcUID,
            ZoneId = c.ZoneId,
            Latitude = c.Latitude,
            Longitude = c.Longitude,
            Description = c.Description,
            IsActive = c.IsActive
        };
    }
}
