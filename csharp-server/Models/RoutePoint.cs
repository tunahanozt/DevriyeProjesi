namespace DevriyeTakip.API.Models
{
    // Rota ile Checkpoint arasındaki sıralı bağ (bir rotanın kaçıncı noktası).
    public class RoutePoint
    {
        public int Id { get; set; }

        public int PatrolRouteId { get; set; }
        public PatrolRoute PatrolRoute { get; set; } = null!;

        public int CheckpointId { get; set; }
        public Checkpoint Checkpoint { get; set; } = null!;

        // Rotadaki sıra numarası (1, 2, 3 ...)
        public int Order { get; set; }
    }
}
