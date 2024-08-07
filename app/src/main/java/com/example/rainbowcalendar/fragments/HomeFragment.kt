package com.example.rainbowcalendar.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.rainbowcalendar.PasswordActivity
import com.example.rainbowcalendar.R
import com.example.rainbowcalendar.RecoveryActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val testButton=view.findViewById<Button>(R.id.testButton)
        val testButton2=view.findViewById<Button>(R.id.testButton2)

        testButton?.setOnClickListener {
            //startActivity(Intent(requireActivity(), PasswordActivity::class.java))
            startActivity(Intent(requireActivity(), RecoveryActivity::class.java))
        }
        val sharedPrefs=requireActivity().getSharedPreferences("com.example.rainbowcalendar_pref", Context.MODE_PRIVATE)

        testButton2?.setOnClickListener {
            sharedPrefs.edit().putString("passwordValue","").apply()
            sharedPrefs.edit().putInt("passwordType",0).apply()

            val sharedPrefTemp=requireActivity().getSharedPreferences("temp",Context.MODE_PRIVATE)
            sharedPrefTemp.edit().putInt("temp",0).apply()
            sharedPrefTemp.edit().putString("temp1","").apply()

            val sharedPrefRecovery=requireActivity().getSharedPreferences("com.example.rainbowcalendar_recovery", Context.MODE_PRIVATE)
            with(sharedPrefRecovery.edit()){
                putString("question1","")
                putString("question2","")
                putString("question3","")
                putString("answer1","")
                putString("answer2","")
                putString("answer3","")
                putBoolean("done",false)
                apply()
            }
        }

    }

    //region generated
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    //endregion
}