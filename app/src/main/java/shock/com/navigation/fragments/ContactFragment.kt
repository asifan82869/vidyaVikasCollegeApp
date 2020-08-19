package shock.com.navigation.fragments

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_contact.*
import shock.com.navigation.R
import shock.com.navigation.email.Gmail
import shock.com.navigation.view.MainActivity

class ContactFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val nAct: MainActivity = activity as MainActivity
        super.onViewCreated(view, savedInstanceState)
        btnSubmit.setOnClickListener {
            if (verifyAvailableNetwork()){
                toastInfo()
            }else{
                Toast.makeText(nAct.contaxt,"on internet connection",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun verifyAvailableNetwork():Boolean{
        val nAct: MainActivity = activity as MainActivity
        val connectivityManager=nAct.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo=connectivityManager.activeNetworkInfo
        return  networkInfo!=null && networkInfo.isConnected
    }

    fun isEmailValid(email: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun toastInfo(){
        val nAct: MainActivity = activity as MainActivity

        val name = edName.text.toString()
        val userId = edUserName.text.toString()
        val subject = edSubject.text.toString()
        var message = edDes.text.toString()
        val recipient = "asifan82869@gmail.com"

        if(isEmailValid(userId)){
            val nameId = "i am $name and My id is $userId\n"
            message = nameId +"\t"+ message

            val sender = Gmail("asifan82869@gmail.com", "asif@1234")

            if (edName.text.isNullOrBlank() && edSubject.text.isNullOrBlank() && edDes.text.isNullOrBlank()){
                Toast.makeText(nAct.contaxt,"Please Enter a data", Toast.LENGTH_LONG).show()
            }else{
                val task = MyTask(nAct.contaxt, sender, subject, message, recipient, recipient)
                task.execute()
                clearAll()
            }
        }else{
            edUserName.setText("")
            edUserName.error = "Email Id must be correct"
        }
    }

    private fun clearAll(){
        edName.setText("")
        edUserName.setText("")
        edSubject.setText("")
        edDes.setText("")
    }

    class MyTask(private val context: Context, var sender: Gmail, var sb:String, var ms:String, var rp:String, var user:String):
        AsyncTask<Void, Void, Void>() {

        var pDialog:ProgressDialog? = null

        override fun onPreExecute() {
            super.onPreExecute()
            pDialog = ProgressDialog(context)
            pDialog!!.setMessage("Please wait...")
            pDialog!!.show()
        }



        override fun doInBackground(vararg p0: Void?): Void? {
            try {
                sender.sendMail(sb,ms,user,rp)
            }catch (e:Exception){
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            pDialog?.cancel()
            Toast.makeText(context, "Send Successfully", Toast.LENGTH_LONG).show()
        }


    }

}