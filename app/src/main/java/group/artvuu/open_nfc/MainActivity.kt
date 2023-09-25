package group.artvuu.open_nfc

import android.graphics.Color
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var isNfcOn = false;
    private var nfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var button = findViewById<ImageButton>(R.id.imageButton)
        var textInput = findViewById<EditText>(R.id.textInput)
        textInput.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        }

        button.setOnClickListener {
            if (isNfcOn) {
                disableNfc()
                button.setColorFilter(Color.argb(255, 0, 0, 0));
            } else{
                button.setColorFilter(Color.argb(255, 0, 255, 0));
                nfcAdapter = NfcAdapter.getDefaultAdapter(this)
                setupNfcBeam(textInput.text.toString())
            }
            isNfcOn = !isNfcOn
        }
        disableNfc()
    }

    private fun disableNfc(){
        nfcAdapter?.disableForegroundNdefPush(this)
        nfcAdapter?.disableForegroundNdefPush(this)
        nfcAdapter?.disableForegroundDispatch(this)
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
    private fun setupNfcBeam(inputText:String) {
        nfcAdapter?.setNdefPushMessageCallback({ _ ->
            val msg = NdefMessage(
                arrayOf(
                    NdefRecord.createMime("text/plain", inputText.toByteArray(Charsets.UTF_8))
                )
            )
            msg
        }, this)
    }
}