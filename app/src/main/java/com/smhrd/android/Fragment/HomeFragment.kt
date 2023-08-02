package com.smhrd.android.Fragment

import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.smhrd.android.Data.BoardIdVO
import com.smhrd.android.Data.HomeCommunityAdapter
import com.smhrd.android.Data.HomeGosuAdapter
import com.smhrd.android.Data.MemberVO
import com.smhrd.android.Data.ReviewVO
import com.smhrd.android.R
import com.smhrd.android.User.LoginActivity
import com.smhrd.android.User.MypageActivity


class HomeFragment : Fragment() {

    lateinit var rvPopularGosu: RecyclerView
    lateinit var rvCommunity: RecyclerView
    lateinit var btnLogin: Button
    lateinit var btnLogout: Button
    lateinit var ibMyPage: ImageButton
    lateinit var ivC: ImageButton
    lateinit var ivCPlus: ImageButton
    lateinit var ivCShap: ImageButton
    lateinit var ivPython: ImageButton
    lateinit var ivJava: ImageButton
    lateinit var ivJS: ImageButton
    lateinit var ivVB: ImageButton
    lateinit var ivPHP: ImageButton

    //
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mAuth = FirebaseAuth.getInstance()

        // inflate 란? xml파일을 View객체로 바꿔주는 것
        var view = inflater.inflate(R.layout.fragment_home, container, false)

        //componet 가져오기
        rvPopularGosu = view.findViewById(R.id.rvPopularGosu)
        rvCommunity = view.findViewById(R.id.rvCommunity)
        btnLogin = view.findViewById(R.id.btnLogin)
        btnLogout = view.findViewById(R.id.btnLogout)
        ibMyPage = view.findViewById(R.id.ibMyPage)
        ivC = view.findViewById(R.id.ivC)
        ivCPlus = view.findViewById(R.id.ivCPlus)
        ivCShap = view.findViewById(R.id.ivCShap)
        ivPython = view.findViewById(R.id.ivPhython)
        ivJava = view.findViewById(R.id.ivJava)
        ivJS = view.findViewById(R.id.ivJS)
        ivVB = view.findViewById(R.id.ivVB)
        ivPHP = view.findViewById(R.id.ivPHP)
        var memberList = ArrayList<MemberVO>()
        var boardList = ArrayList<BoardIdVO>()
        var review = ArrayList<ReviewVO>()
        boardList.add(BoardIdVO("제목1", "내용1", "작성자1", "20230802", "", 18, 3, null ))
        boardList.add(BoardIdVO("제목2", "내용2", "작성자2", "20230805", "", 8, 1, null ))
        boardList.add(BoardIdVO("제목3", "내용3", "작성자3", "20230807", "", 28, 15, null ))


        memberList.add(MemberVO("asdf", "01000000000", "asdf"))
        memberList.add(MemberVO("qwer", "01000000000", "qwer"))
        memberList.add(MemberVO("zxcv", "01000000000", "zxcv"))
        review.add(ReviewVO("qwer", "좋아요", "", "20230802", 5))
        review.add(ReviewVO("asdf", "짱", "", "20230802", 2))
        review.add(ReviewVO("zxcv", "최고", "", "20230802", 3))

        //인기있는 고수 출력
        rvPopularGosu.layoutManager = GridLayoutManager(context, 2)
        var adapter = HomeGosuAdapter(memberList, review, requireContext())
        rvPopularGosu.adapter = adapter



        //커뮤니티
        rvCommunity.layoutManager = GridLayoutManager(context, 2)
        var adapter2 = HomeCommunityAdapter(boardList, requireContext())
        rvCommunity.adapter = adapter2

        //C 클릭했을 때
        ivC.setOnClickListener {
//            val c = Intent(requireActivity(), )
        }
        //C++ 클릭했을 때
        ivCPlus.setOnClickListener {

        }
        //C# 클릭했을 때
        ivCShap.setOnClickListener {

        }
        //Python 클릭했을 때
        ivPython.setOnClickListener {

        }
        //Java 클릭했을 때
        ivJava.setOnClickListener {

        }
        //JavaScript 클릭했을 때
        ivJS.setOnClickListener {

        }
        //Visual Basic 클릭했을 때
        ivVB.setOnClickListener {

        }
        //PHP 클릭했을 때
        ivPHP.setOnClickListener {

        }

        val spf = activity?.getSharedPreferences("memberInfoSpf", Context.MODE_PRIVATE)
        var loginMember = spf?.getString("memberId", "")
        Log.d("loginMember", loginMember.toString())


        //로그인 버튼 클릭했을 때
        btnLogin.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)

        }

        if (loginMember.toString() == "") {
            //로그인 안되어 있을 때
            btnLogout.visibility = View.INVISIBLE
        } else {
            //로그인되어 있을 때
            btnLogin.visibility = View.INVISIBLE
            btnLogout.setOnClickListener {
                Toast.makeText(context, "로그아웃되었습니다.", Toast.LENGTH_SHORT).show()
                var editor = spf?.edit()
                editor?.remove("memberId")
                editor?.commit()
                btnLogout.visibility = View.INVISIBLE
                btnLogin.visibility = View.VISIBLE
            }
        }


        //마이페이지 버튼 클릭했을 때
        ibMyPage.setOnClickListener {
            val intent = Intent(requireActivity(), MypageActivity::class.java)
            startActivity(intent)
        }

//        btnLogout.setOnClickListener {
//            val sharedPreferences = requireActivity().getSharedPreferences("memberInfoSpf", Context.MODE_PRIVATE)
//            val editor = sharedPreferences.edit()
//            editor.remove("memberId")
//            editor.apply()
//
//            btnLogout.visibility = View.INVISIBLE
//            btnLogin.visibility = View.VISIBLE
//        }





        return view
    }

}
