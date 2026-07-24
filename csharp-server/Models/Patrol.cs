namespace DevriyeTakip.API.Models
{
    // Bir personele atanan devriye örneği (bir rotanın bir kez yürütülmesi).
    public class Patrol
    {
        public int Id { get; set; }

        public int UserId { get; set; }        // Atanan personel
        public User User { get; set; } = null!;

        public int PatrolRouteId { get; set; } // Hangi rota
        public PatrolRoute PatrolRoute { get; set; } = null!;

        public PatrolStatus Status { get; set; } = PatrolStatus.Atandi;

        public DateTime AssignedAt { get; set; } = DateTime.UtcNow;
        public DateTime? StartedAt { get; set; }
        public DateTime? CompletedAt { get; set; }

        public string? InterruptReason { get; set; } // Yarıda kesildiyse sebebi

        // Bu devriyede yapılan okumalar
        public ICollection<PatrolScan> Scans { get; set; } = new List<PatrolScan>();
    }
}
