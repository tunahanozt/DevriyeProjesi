package com.garoz.devriyemobil.android

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.garoz.devriyemobil.App
import com.garoz.devriyemobil.android.nfc.NfcManager

class MainActivity : ComponentActivity() {

    private lateinit var nfcManager: NfcManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO (Aşama 3 - Offline Senkronizasyon):
        // Periyodik WorkManager senkronizasyonu (DevriyeSyncWorker) buradan
        // DevriyeApplication'a taşınacak. WorkManager bağımlılığı shared/androidMain'de
        // olduğu için kurulumun orada yapılması gerekiyor.

        // NFC modülünü başlat ve okunan UID'yi dinle
        nfcManager = NfcManager(this) { nfcUid ->
            // TODO (Aşama 2): UID doğrudan DevriyeKayitUseCase'e aktarılacak (şimdilik bilgi amaçlı)
            Toast.makeText(this, "NFC Okundu UID: $nfcUid", Toast.LENGTH_SHORT).show()
        }

        if (!nfcManager.isNfcAvailable()) {
            Toast.makeText(this, "Bu cihazda NFC özelliği bulunmuyor!", Toast.LENGTH_LONG).show()
        }

        // Compose arayüzünü göster (Login ekranı shared modüldeki App() içinde)
        setContent {
            App()
        }
    }

    // NFC dinlemesini Activity yaşam döngüsüne bağlıyoruz
    override fun onResume() {
        super.onResume()
        nfcManager.enableForegroundDispatch()
    }

    override fun onPause() {
        super.onPause()
        nfcManager.disableForegroundDispatch()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        nfcManager.processIntent(intent)
    }
}
