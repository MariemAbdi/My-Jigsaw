package com.example.myjigsaw

import android.content.Context
import android.content.Intent
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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var pass: AppCompatEditText
    lateinit var clearEmail : ImageView
    lateinit var clearPass : ImageView
    val handler = Handler()
    lateinit var dialoglayout: View
    private lateinit var  auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_layout)

        email = findViewById(R.id.loginEmail)
        clearEmail = findViewById(R.id.clearEmail)
        pass = findViewById(R.id.loginPassword)
        clearPass = findViewById(R.id.clearPassword)
        auth= FirebaseAuth.getInstance()

        email.addTextChangedListener(object: TextWatcher {
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

        pass.addTextChangedListener(object: TextWatcher {
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

    override fun onBackPressed() {
        this.finishAffinity()
    }

    fun goToRegister(view: View) {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }

    private fun ShowTaost(msg:String){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }

    fun login(view: View) {
        val userEmail= email.text.toString()
        val userPassword= pass.text.toString()

        if(userEmail.isEmpty()){
            ShowTaost("Please Enter Your Email")
        }else if(userPassword.isEmpty()){
            ShowTaost("Please Enter Your Password")
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            ShowTaost("Please Enter A Valid Email")
        }else if (userPassword.length<6){
            ShowTaost("Password Should Have At Least 6 Characters")
        }else{
            //Check If User Exists In The Firebase Auth
            auth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener { task ->
                //If User Exists
                if(task.isSuccessful) {
                    //Clear Fields
                    clearEmailField(view)
                    clearPasswordField(view)
                    //Create An Alert Builder To Show Message & Show It
                    val builder = AlertDialog.Builder(this, R.style.MyDialogTheme)
                    builder.setCancelable(false)
                    val inflater = layoutInflater;
                    dialoglayout = inflater.inflate(R.layout.logged_successfully, null);
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


                    //Set loggedIn To True
                    val sharedPreference =
                        getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
                    var editor = sharedPreference.edit()
                    editor.putBoolean("loggedIn", true)
                    editor.commit()
                    //Go To The Home Page
                    handler.postDelayed({
                        goToHomePage()
                    }, 1000)
                }else{
                    ShowTaost("Please Verify Your Coordinates.")
                }
            }.addOnFailureListener { exception ->
                ShowTaost(exception.localizedMessage)
            }
        }
    }

    fun goToHomePage() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun clearEmailField(view: View) {
        email.text!!.clear()
    }

    fun clearPasswordField(view: View) {
        pass.text!!.clear()
    }

    fun ResetPassword(view: View) {
        val intent = Intent(this, ResetPasswordActivity::class.java)
        startActivity(intent)
    }
}