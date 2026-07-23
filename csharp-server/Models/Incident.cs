using System;

namespace DevriyeTakip.API.Models
{
    public class Incident
    {
        public int Id { get; set; }
        public int UserId { get; set; } // Hangi görevli bildirdi?
        public int? CheckpointId { get; set; } // Belirli bir noktadaysa ID'sini tutarız (Nullable)

        public string IssueType { get; set; } = string.Empty; // Örn: "Su Baskını"
        public string Description { get; set; } = string.Empty;
        public string? PhotoUrl { get; set; } // Çekilen fotoğrafın sunucudaki yolu

        public DateTime ReportedAt { get; set; } = DateTime.UtcNow;
        public string Status { get; set; } = "Açık"; // Açık, İnceleniyor, Çözüldü
    }
}