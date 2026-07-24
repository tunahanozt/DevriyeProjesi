namespace DevriyeTakip.API.Models
{
    // Bir devriye rotası: sıralı kontrol noktalarından oluşur.
    // (Çerçevedeki Microsoft.AspNetCore.Routing.Route ile karışmaması için "PatrolRoute" adı seçildi.)
    public class PatrolRoute
    {
        public int Id { get; set; }

        public string Name { get; set; } = string.Empty; // Örn: "A Blok Gece Turu"
        public string? Description { get; set; }
        public bool IsActive { get; set; } = true;

        // Rotadaki sıralı noktalar
        public ICollection<RoutePoint> Points { get; set; } = new List<RoutePoint>();
    }
}
