using Microsoft.AspNetCore.SignalR;

namespace DevriyeTakip.API.Hubs
{
    // Sistemdeki gerçek zamanlı iletişimin ana dağıtım merkezi
    public class LiveTrackingHub : Hub
    {
        // İleride özel yetkilendirmeler veya "Sadece A bölgesindeki güvenliğe mesaj at" 
        // gibi işlemler için içini dolduracağız. Şimdilik sadece tünel olarak açık kalması yeterli.
    }
}