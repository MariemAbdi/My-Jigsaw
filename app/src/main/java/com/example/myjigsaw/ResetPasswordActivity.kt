package com.example.myjigsaw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var clearEmail : ImageView
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        email=findViewById(R.id.resetEmail)
        clearEmail=findViewById(R.id.clearEmail)
        auth = FirebaseAuth.getInstance()

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

    }

    fun clearEmailField(view: View) {
        email.text.clear()
    }

    private fun ShowTaost(msg:String){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }

    fun resetPassword(view: View) {
        val UserEmail=email.text.toString()

        if(UserEmail.isEmpty()){
            ShowTaost("Please Type Your Email")
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(UserEmail).matches()){
            ShowTaost("Please Enter A Valid Email")
        }else{
            auth!!.sendPasswordResetEmail(UserEmail).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    ShowTaost("An Email Was Sent To $UserEmail")
                } else {
                    ShowTaost("Something Went Wrong.")
                }
            } }
    }
}