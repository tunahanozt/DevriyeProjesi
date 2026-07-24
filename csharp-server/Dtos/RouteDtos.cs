namespace DevriyeTakip.API.Dtos
{
    // İstek: yeni rota oluşturma
    public class CreateRouteDto
    {
        public string Name { get; set; } = string.Empty;
        public string? Description { get; set; }
    }

    // İstek: rotaya sıralı nokta ekleme
    public class AddRoutePointDto
    {
        public int CheckpointId { get; set; }
        public int Order { get; set; }
    }

    // Yanıt: rotadaki bir nokta
    public class RoutePointDto
    {
        public int Id { get; set; }
        public int CheckpointId { get; set; }
        public string CheckpointName { get; set; } = string.Empty;
        public int Order { get; set; }
    }

    // Yanıt: rota ve sıralı noktaları
    public class RouteDto
    {
        public int Id { get; set; }
        public string Name { get; set; } = string.Empty;
        public string? Description { get; set; }
        public bool IsActive { get; set; }
        public List<RoutePointDto> Points { get; set; } = new();
    }
}
