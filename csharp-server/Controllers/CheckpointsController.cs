using DevriyeTakip.API.Models;
using DevriyeTakip.API.Hubs; // YENİ EKLENDİ
using DevriyeTakip.API.Repositories;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.SignalR; // YENİ EKLENDİ

namespace DevriyeTakip.API.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [Authorize]
    public class CheckpointsController : ControllerBase
    {
        private readonly ICheckpointRepository _repository;
        private readonly IHubContext<LiveTrackingHub> _hubContext; // SİNYAL MERKEZİMİZ

        // Dependency Injection ile Hub'ı Controller'a bağlıyoruz
        public CheckpointsController(ICheckpointRepository repository, IHubContext<LiveTrackingHub> hubContext)
        {
            _repository = repository;
            _hubContext = hubContext;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<Checkpoint>>> GetAllCheckpoints()
        {
            var noktalar = await _repository.GetAllAsync();
            return Ok(noktalar);
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
    }
}