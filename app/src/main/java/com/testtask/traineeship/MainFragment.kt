package com.testtask.traineeship

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.testtask.traineeship.databinding.FragmentMainBinding


class MainFragment : Fragment() {



    private var _binding: FragmentMainBinding? = null
    private val binding get () = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
    }


    private fun initButtons() {
        binding.austria.setOnClickListener { openFragment(AUS) }
        binding.Italy.setOnClickListener { openFragment(ITA) }
        binding.Poland.setOnClickListener { openFragment(POL) }
        binding.colombia.setOnClickListener { openFragment(COL) }
        binding.denmark.setOnClickListener { openFragment(DEN) }
        binding.madagascar.setOnClickListener { openFragment(MAD) }
        binding.switzerland.setOnClickListener { openFragment(SWI) }
        binding.thailand.setOnClickListener { openFragment(THA) }
        binding.task2.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, Task2Fragment.newInstance())
                .addToBackStack(TASK2)
                .commit()
        }
    }

    private fun openFragment(country: String) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, FlagFragment.newInstance(country))
            .addToBackStack("")
            .commit()
    }


    companion object {

        fun newInstance() = MainFragment()


        const val TASK2 = "task2"
        const val AUS = "Austria"
        const val ITA = "Italy"
        const val POL = "Poland"
        const val COL = "Colombia"
        const val DEN = "Denmark"
        const val MAD = "Madagascar"
        const val SWI = "Switzerland"
        const val THA = "Thailand"

    }
}