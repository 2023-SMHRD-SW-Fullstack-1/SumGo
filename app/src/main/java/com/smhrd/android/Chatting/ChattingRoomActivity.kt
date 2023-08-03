package com.smhrd.android.Chatting

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.smhrd.android.Data.ChatVO
import com.smhrd.android.Data.DummyChatItemVO
import com.smhrd.android.R
import java.util.Date
import java.util.Locale

class ChattingRoomActivity : AppCompatActivity() {

    lateinit var rv: RecyclerView
    lateinit var edtMsg: EditText
    lateinit var btnSend: Button

    val database = Firebase.database

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting_room)

        rv = findViewById(R.id.rv)
        btnSend = findViewById(R.id.btnSend)
        edtMsg = findViewById(R.id.edtMsg)

        val spf = getSharedPreferences("memberInfoSpf", MODE_PRIVATE)
        var userId = spf.getString("memberId", null).toString()
        var intent = getIntent()
        var roomId = intent.getStringExtra("roomId")

        // 상대방ID 추출
        val chatUserId = (roomId!!.trim().split(" "))[2]

        // DB에 roomId가 없으면 저장
        checkAndSaveRoomId(userId, roomId)
        checkAndSaveRoomId(chatUserId, roomId)

        val chatInputRef = database.getReference("roomList")

        var data = ArrayList<ChatVO>()

        var adapter = ChatAdapter(applicationContext, data)

        rv.layoutManager = LinearLayoutManager(applicationContext)
        rv.adapter = adapter

        btnSend.setOnClickListener {
            var msgTime = chatInputTime()
            var msgContent = edtMsg.text.toString()

            chatInputRef.push().setValue(ChatVO(userId, msgContent, msgTime))

            rv.smoothScrollToPosition(data.size - 1)

            edtMsg.text.clear()
        }
        chatInputRef.addChildEventListener(ChatChildEvent(data, adapter))
    }

    fun checkAndSaveRoomId(userId: String, roomId: String) {
        val chatRoomRef = database.getReference("memberList").child(userId).child("chatRoom")

        chatRoomRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chatRoomList = mutableListOf<String>()

                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val chatRoomId = i.getValue(String::class.java)
                        chatRoomId?.let {
                            chatRoomList.add(it)
                        }
                    }
                }
                if (!chatRoomList.contains(roomId)) {
                    chatRoomList.add(roomId)
                    chatRoomRef.setValue(chatRoomList).addOnSuccessListener {
                        println("roomId가 저장되었습니다.")
                    }.addOnFailureListener {
                        println("roomId 저장에 실패했습니다: ${it.message}")
                    }
                } else {
                    println("roomId가 이미 존재합니다.")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("roomId 조회에 실패했습니다: ${databaseError.message}")
            }
        })
    }

    fun getKoreanAMPM(hour: Int): String {
        return if (hour < 12) "오전" else "오후"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun chatInputTime(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentTimeMillis
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)

        // 오후 04:30, 오전 11:24 형식으로 시간을 출력하기 위한 패턴
        val pattern = "hh:mm"
        val formattedTime =
            SimpleDateFormat(pattern, Locale.getDefault()).format(Date(currentTimeMillis))
        val koreanAMPM = getKoreanAMPM(hourOfDay)
        val finalFormattedTime = "$koreanAMPM $formattedTime"

        return finalFormattedTime
    }
}