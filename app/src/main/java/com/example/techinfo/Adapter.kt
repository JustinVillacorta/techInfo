import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.techinfo.ComponentData
import com.example.techinfo.R

class Adapter(
    private val componentList: List<ComponentData>
) : RecyclerView.Adapter<Adapter.ComponentViewHolder>() {

    // ViewHolder class to hold references to the views in each item
    class ComponentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val componentImageView: ImageView = view.findViewById(R.id.image)
        val componentNameTextView: TextView = view.findViewById(R.id.title)
    }

    // Inflate the item layout and create the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item, parent, false)
        return ComponentViewHolder(view)
    }

    // Bind data to the ViewHolder (populate the item with data)
    override fun onBindViewHolder(holder: ComponentViewHolder, position: Int) {
        val component = componentList[position]
        holder.componentNameTextView.text = component.name

        // If you have images associated with components, set them here. Example:
        // holder.componentImageView.setImageResource(component.imageResId)
        // Otherwise, leave it out or set a default image
        holder.componentImageView.setImageResource(R.drawable.ic_launcher_foreground) // Example placeholder
    }

    // Return the total count of items in the list
    override fun getItemCount(): Int {
        return componentList.size
    }
}
