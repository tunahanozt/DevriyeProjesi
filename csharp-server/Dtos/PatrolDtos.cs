namespace DevriyeTakip.API.Dtos
{
    // İstek: bir personele rota atama (devriye oluşturma)
    public class AssignPatrolDto
    {
        public int UserId { get; set; }
        public int PatrolRouteId { get; set; }
    }

    // Yanıt: devriye özeti
    public class PatrolDto
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public string PersonelAdi { get; set; } = string.Empty;
        public int PatrolRouteId { get; set; }
        public string RotaAdi { get; set; } = string.Empty;
        public string Status { get; set; } = string.Empty;
        public DateTime AssignedAt { get; set; }
        public DateTime? StartedAt { get; set; }
        public DateTime? CompletedAt { get; set; }
    }
}
