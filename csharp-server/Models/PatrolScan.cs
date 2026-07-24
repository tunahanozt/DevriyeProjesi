namespace DevriyeTakip.API.Models
{
    // Bir devriye sırasında bir kontrol noktasının okutulması.
    public class PatrolScan
    {
        public int Id { get; set; }

        public int PatrolId { get; set; }
        public Patrol Patrol { get; set; } = null!;

        public int CheckpointId { get; set; }
        public Checkpoint Checkpoint { get; set; } = null!;

        public DateTime ScannedAt { get; set; } = DateTime.UtcNow;

        // GPS (Faz 4'te konum doğrulaması için) - opsiyonel
        public double? Latitude { get; set; }
        public double? Longitude { get; set; }
        public bool IsLocationVerified { get; set; } = false;
    }
}
