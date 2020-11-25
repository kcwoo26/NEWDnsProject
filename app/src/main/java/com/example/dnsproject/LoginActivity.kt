package com.example.dnsproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dnsproject.exeClasses.Exercise
import com.example.dnsproject.exeClasses.Routine
import com.example.dnsproject.exeClasses.User
import com.google.firebase.database.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private val userRef = FirebaseDatabase.getInstance().reference
    private var flag = false
//    private var sp=getSharedPreferences("isLogin", Context.MODE_PRIVATE)
//   val edit : SharedPreferences.Editor=sp.edit()

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        LoginButton.setOnClickListener {
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {

                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (i in snapshot.children) {
                            Log.d("db", i.child("pw").toString())
                            if (i.child("pw").value == PwText.text.toString() && i.child("id").value == IdText.text.toString()) {
                                Log.d("db",i.value.toString())
                                val user = i.value as HashMap<Any, User>
                                Log.d("db", "맞음 ")
                                flag = !flag
                                var userData= Gson().fromJson(i.value.toString(),User::class.java)
                                Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_LONG).show()
                                if(flag){
                                    val nextIntent = Intent(this@LoginActivity, MainActivity::class.java)
                                    nextIntent.putExtra("nameKey", userData)
                                    startActivity(nextIntent)
                                }
                                break
                            } else {
                                Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
            })
        }

        SignUpButton.setOnClickListener {
            if (IdText.text.isNotEmpty() && PwText.text.isNotEmpty()) {
                val exerciseList = ArrayList<Exercise>()
                exerciseList.add(Exercise("default", "0kg", "0회"))
                exerciseList.add(Exercise("default2", "20kg", "20회"))
                val routineList=ArrayList<Routine>()
                routineList.add(Routine("첫번째", exerciseList))
                val user = User( IdText.text.toString(),
                    PwText.text.toString(),
                    routineList)
                userRef.push().setValue(user)
                IdText.text.clear()
                PwText.text.clear()
            }
        }
    }
}

