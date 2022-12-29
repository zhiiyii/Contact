package my.edu.tarc.contact

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import my.edu.tarc.contact.databinding.FragmentFirstBinding
import my.edu.tarc.contact.viewmodel.ContactViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val contactViewModel: ContactViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("First Fragment", "onViewCreated")

        val fab: View? = requireActivity().findViewById(R.id.fab)
        fab!!.isVisible = true

        // Attach adapter to the RecyclerView
        val contactAdapter = ContactAdapter()

        // check for changes, contactList is live data
        contactViewModel.contactList.observe(viewLifecycleOwner) {
            contactAdapter.setContact(it)
        }

        with(binding.recyclerViewContact) {
            layoutManager = LinearLayoutManager(requireActivity())
//            contactAdapter.setContact(MainActivity.contactList)
            adapter = contactAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("First Fragment", "onDestroyView")
    }
}