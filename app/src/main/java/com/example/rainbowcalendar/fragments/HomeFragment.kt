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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.example.rainbowcalendar.PasswordActivity
import com.example.rainbowcalendar.R

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
            startActivity(Intent(requireActivity(), PasswordActivity::class.java))
        }

        testButton2?.setOnClickListener {
            val sharedPrefPasswordText=requireActivity().getSharedPreferences("com.example.rainbowcalendar_passwordtext", Context.MODE_PRIVATE)
            with(sharedPrefPasswordText.edit()){
                putString("com.example.rainbowcalendar_passwordtext","")
                apply()
            }
            val sharedPrefPasswordType=requireActivity().getSharedPreferences("com.example.rainbowcalendar_passwordType", Context.MODE_PRIVATE)
            with(sharedPrefPasswordType.edit()){
                putInt("com.example.rainbowcalendar_passwordType",0)
                apply()
            }
            val sharedPrefType=requireActivity().getSharedPreferences("temp",Context.MODE_PRIVATE)
            with(sharedPrefType.edit()){
                putInt("temp",0)
                apply()
            }
            val sharedPrefPassTemp=requireActivity().getSharedPreferences("temp1",Context.MODE_PRIVATE)
            with(sharedPrefPassTemp.edit()){
                putString("temp1","")
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
        //wtf is this
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