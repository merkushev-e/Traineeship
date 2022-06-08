package com.testtask.traineeship.presentation.view

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import coil.api.load
import com.testtask.traineeship.R
import com.testtask.traineeship.databinding.FragmentContactDetailsBinding
import com.testtask.traineeship.databinding.FragmentContactListBinding
import com.testtask.traineeship.domain.model.Contact
import kotlinx.android.synthetic.main.fragment_contact_details.*
import kotlinx.android.synthetic.main.fragment_contact_details.view.*


class ContactDetailsFragment : Fragment() {

    private var _binding: FragmentContactDetailsBinding? = null
    private val binding get() = _binding!!
    private var prevNumb: String? = null
    private var contact: Contact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            contact = it.getParcelable(CONTACT)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_details, container, false)
        _binding = FragmentContactDetailsBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (contact != null) {
            prevNumb = contact?.number
            binding.contactsName.setText(contact?.name)
            binding.contactsLastName.setText(contact?.lastName)
            binding.contactsPhone.contacts_phone.setText(contact?.number)
            binding.imageView.load("https://picsum.photos/id/${contact?.picId}/300/300")
        }


        binding.button.setOnClickListener {

            val contact =
                contact?.picId?.let { it1 ->
                    Contact(
                        binding.contactsPhone.text.toString(),
                        binding.contactsName.text.toString(),
                        binding.contactsLastName.text.toString(),
                        it1
                    )
                }

            val bundle = Bundle().apply {
                putParcelable(CONTACT_RESULT_BUNDLE, contact)
                putString(CONTACT_RESULT_PREV_VAL,prevNumb)
            }
            setFragmentResult(CONTACT_RESULT,bundle)

            parentFragmentManager.popBackStack()
        }
    }

    companion object {

        const val CONTACT = "CONTACT"
        const val CONTACT_RESULT = "CONTACT_RESULT"
        const val CONTACT_RESULT_BUNDLE = "CONTACT_RESULT"
        const val CONTACT_RESULT_PREV_VAL = "PREV_VAL"


        @JvmStatic
        fun newInstance(contact: Contact) =
            ContactDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CONTACT, contact)
                }
            }
    }
}