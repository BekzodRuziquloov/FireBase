package space.beka.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import space.beka.firebase.adapter.RvAdapter
import space.beka.firebase.databinding.ActivityRealTimeBinding

class RealTimeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRealTimeBinding
    lateinit var firebaseDatabase:FirebaseDatabase
    lateinit var reference: DatabaseReference
    private lateinit var rvAdapter: RvAdapter
    private lateinit var list: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRealTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("messages")
        binding.btnSend.setOnClickListener {
            val message = binding.edtMessage.text.toString().trim()
            if (message.isNotEmpty()){
                val key =reference.push().key
                reference.child(key!!).setValue(message)
                binding.edtMessage.text.clear()
                Toast.makeText(this, "Send message", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Write something", Toast.LENGTH_SHORT).show()
            }
        }
        list = ArrayList()
        rvAdapter = RvAdapter(list)
binding.rvMessage.adapter = rvAdapter
        reference.addValueEventListener(
            object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (child in snapshot.children){
                        list.add(child.value.toString())
                    }
                    rvAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@RealTimeActivity, "Error!!", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}