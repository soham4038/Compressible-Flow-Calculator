package com.astrodev.flowcalc

import android.R.layout
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var about: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = view.findViewById(R.id.list)
        about = view.findViewById(R.id.floatingActionButton)
        val calcFragmentOption = arrayOf(
            "Isentropic Flow",
            "Normal Shock",
            "Oblique Shock",
            "Conical Shock",
            "Fanno Flow"
        )

        val arrayAdapter = ArrayAdapter(
            activity?.baseContext!!,
            layout.simple_list_item_1, calcFragmentOption
        )
        listView.adapter = arrayAdapter
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, id ->
                (layout_main.parent as ViewGroup).removeView(layout_main)
                val manager = childFragmentManager
                manager.beginTransaction()
                when (position) {
                    0 -> {
                        Navigation.findNavController(view)
                            .navigate(R.id.action_mainFragment_to_isentropic)
                    }
                    1 -> {
                        Navigation.findNavController(view)
                            .navigate(R.id.action_mainFragment_to_normalShock)
                    }
                    2 -> {
                        Navigation.findNavController(view)
                            .navigate(R.id.action_mainFragment_to_obliqueShock)

                    }
                    3 -> {
                        Navigation.findNavController(view)
                            .navigate(R.id.action_mainFragment_to_conicalShock)
                    }
                    4 -> {
                        Navigation.findNavController(view)
                            .navigate(R.id.action_mainFragment_to_fannoFlow)
                    }
                }
            }

        about.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
            builder.setTitle("About")
            builder.setCancelable(true)
            val customLayout: View = layoutInflater.inflate(R.layout.info, null)
            builder.setView(customLayout)
            builder.setNeutralButton("OK", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                }
            })
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
}