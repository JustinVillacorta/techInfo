import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.techinfo.R

class AlertDialog_Buildpc : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use a custom style to make the dialog background transparent
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCancelable(false) // Optional: Make dialog non-cancelable
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for the dialog (CardView-based)
        val view = inflater.inflate(R.layout.fragment_alert_dialog__buildpc, container, false)

        // Find and set up the "OK" button in the CardView
        val okbtn: Button = view.findViewById(R.id.okbtn)

        // Set up button click listener to dismiss the dialog
        okbtn.setOnClickListener {
            dismiss() // Dismiss the dialog when "OK" is clicked
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        // Make sure dialog is styled correctly
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
