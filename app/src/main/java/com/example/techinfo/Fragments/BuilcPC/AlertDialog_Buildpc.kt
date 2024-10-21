import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.techinfo.R

class AlertDialog_Buildpc(private val message: String) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCancelable(false)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alert_dialog__buildpc, container, false)

        // Set up the "OK" button
        val okbtn: Button = view.findViewById(R.id.okbtn)
        okbtn.setOnClickListener {
            dismiss() // Dismiss the dialog when "OK" is clicked
        }

        // Display the issues or error message in the dialog
        val dialogMessage: TextView = view.findViewById(R.id.dialogMessage)
        dialogMessage.text = message

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
