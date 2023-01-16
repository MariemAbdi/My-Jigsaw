package com.example.myjigsaw

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {

    lateinit var name: EditText
    lateinit var email: EditText
    lateinit var pass: EditText
    lateinit var clearName : ImageView
    lateinit var clearEmail : ImageView
    lateinit var clearPass : ImageView
    val handler = Handler()
    lateinit var dialoglayout: View
    private lateinit var  auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        name = findViewById(R.id.registerUsername)
        clearName = findViewById(R.id.clearUsername)
        email = findViewById(R.id.registerEmail)
        clearEmail = findViewById(R.id.clearEmail)
        pass = findViewById(R.id.registerPassword)
        clearPass = findViewById(R.id.clearPassword)
        auth= FirebaseAuth.getInstance()

        name.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (name.text.toString().isNotEmpty()){
                    clearName.visibility= View.VISIBLE
                }else{
                    clearName.visibility=View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        email.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (email.text.toString().isNotEmpty()){
                    clearEmail.visibility= View.VISIBLE
                }else{
                    clearEmail.visibility=View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        pass.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (pass.text.toString().isNotEmpty()){
                    clearPass.visibility= View.VISIBLE
                }else{
                    clearPass.visibility=View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun ShowTaost(msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }

    fun register(view: View) {
        val nickname= name.text.toString()
        val userEmail= email.text.toString()
        val userPassword= pass.text.toString()

        if(nickname.isEmpty()){
            ShowTaost("Please Type Your Username")
        }else if(userEmail.isEmpty()){
            ShowTaost("Please Type Your Email")
        }else if(userPassword.isEmpty()){
            ShowTaost("Please Type Your Password")
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            ShowTaost("Please Enter A Valid Email")
        }else if (userPassword.length<6){
            ShowTaost("Password Should Have At Least 6 Characters")
        }else{
            //Check If User Exists Already Else Create Account
            auth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    clearUsernameField(view)
                    clearEmailField(view)
                    clearPasswordField(view)
                    val builder = AlertDialog.Builder(this)
                    val inflater = layoutInflater;
                    dialoglayout = inflater.inflate(R.layout.registered_successfully, null);

                    builder.setView(dialoglayout);
                    val dialog = builder.create()
                    dialog.show()

                    val displayMetrics = DisplayMetrics()
                    windowManager.defaultDisplay.getMetrics(displayMetrics)
                    val displayWidth = displayMetrics.widthPixels
                    val displayHeight = displayMetrics.heightPixels
                    val layoutParams = WindowManager.LayoutParams()
                    layoutParams.copyFrom(dialog.window!!.attributes)
                    val dialogWindowHeight = (displayHeight * 0.5f).toInt()
                    layoutParams.width = displayWidth
                    layoutParams.height = dialogWindowHeight
                    dialog.window!!.attributes = layoutParams

                    handler.postDelayed({
                        goToLogin(view)
                    },2000)
                }else{
                    ShowTaost("User Exists Already!")
                }
            }.addOnFailureListener { exception ->
                ShowTaost(exception.localizedMessage)
            }
        }
    }

    fun goToLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun clearUsernameField(view: View) {
        name.text.clear()
    }
    fun clearEmailField(view: View) {
        email.text.clear()
    }
    fun clearPasswordField(view: View) {
        pass.text.clear()
    }

}