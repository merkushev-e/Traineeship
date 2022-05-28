package com.testtask.traineeship

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

private const val COUNTRY_PARAM = "country"

class FlagFragment : Fragment() {
    private var countryParam: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
            countryParam = it.getString(COUNTRY_PARAM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        when(countryParam){
            MainFragment.AUS -> return inflater.inflate(R.layout.austria, container, false)
            MainFragment.ITA -> return inflater.inflate(R.layout.italy, container, false)
            MainFragment.POL -> return inflater.inflate(R.layout.poland, container, false)
            MainFragment.COL  -> return inflater.inflate(R.layout.colombia, container, false)
            MainFragment.DEN  -> return inflater.inflate(R.layout.denmark, container, false)
            MainFragment.SWI  -> return inflater.inflate(R.layout.switzerland, container, false)
            MainFragment.THA -> return inflater.inflate(R.layout.thailand, container, false)
            MainFragment.MAD  -> return inflater.inflate(R.layout.madagaskar, container, false)

        }
        return inflater.inflate(R.layout.fragment_flag, container, false)

    }

    companion object {
        fun newInstance(country: String) = FlagFragment().apply {
            arguments = Bundle().apply {
                putString(COUNTRY_PARAM, country)
            }
        }

    }
}