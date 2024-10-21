import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.techinfo.R

class AlertDialog_Buildpc(private val message: String) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        // Inflate the custom layout for the dialog
        val view = LayoutInflater.from(requireActivity()).inflate(R.layout.fragment_alert_dialog__buildpc, null)

        // Display the message in the dialog
        val dialogMessage: TextView = view.findViewById(R.id.dialogMessage)
        dialogMessage.text = message

        // Set the custom view and the default "OK" button
        builder.setView(view)
            .setCancelable(false) // Make the dialog not cancellable by tapping outside


        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}
