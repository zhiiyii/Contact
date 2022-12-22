package my.edu.tarc.contact

import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.PatternMatcher
import android.service.controls.actions.FloatAction
import android.util.Patterns
import android.view.*
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import my.edu.tarc.contact.databinding.FragmentSecondBinding
import my.edu.tarc.contact.model.Profile
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(), MenuProvider {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val getPhoto = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.imageViewProfile.setImageURI(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fab : View? = requireActivity().findViewById(R.id.fab)
        fab!!.isVisible = false

        // Enable menu handling here
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.imageViewProfile.setOnClickListener {
            // Associate result from an Activity
            getPhoto.launch("image/*")
        }
    }

    override fun onStart() {
        super.onStart()

        val sharedPref = activity?.getSharedPreferences(getString(R.string.profile_pref),
            Context.MODE_PRIVATE)?: return

        if (sharedPref.contains(getString(R.string.name))) {
            binding.editTextName.setText(sharedPref.getString(getString(R.string.name), ""))
        }
        if (sharedPref.contains(getString(R.string.phone))) {
            binding.editTextPhone.setText(sharedPref.getString(getString(R.string.phone), ""))
        }
        if (sharedPref.contains(getString(R.string.email))) {
            binding.editTextEmail.setText(sharedPref.getString(getString(R.string.email), ""))
        }

        readProfilePicture()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        val fab : View? = requireActivity().findViewById(R.id.fab)
        fab!!.isVisible = true
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.findItem(R.id.action_settings).isVisible = false
        menu.findItem(R.id.action_profile).isVisible = false
        menu.findItem(R.id.action_save).isVisible = true
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.action_save) {
            if (binding.editTextName.text.toString().isNullOrEmpty()){
                binding.editTextName.error = getString(R.string.invalid_input)
                return false
            }
            if (!Helper().isValidEmail(binding.editTextEmail.text.toString())) {
                binding.editTextEmail.error = getString(R.string.invalid_input)
                return false
            }
            if (!Helper().isValidPhone(binding.editTextPhone.text.toString())) {
                binding.editTextPhone.error = getString(R.string.invalid_input)
                return false
            }

            /*
            val pref = activity?.getPreferences(Context.MODE_PRIVATE)?: return true
            with (pref.edit()) {
                val profile = Profile()
                putString("name", profile.name)
                putString("phone", profile.phone)
                putString("email", profile.email)
            } */

            // Create a reference to the Shared Preference
            val sharedPref = activity?.getSharedPreferences(getString(R.string.profile_pref),
                Context.MODE_PRIVATE)?: return true

            with (sharedPref.edit()) {
                val profile = Profile(
                    binding.editTextName.text.toString(),
                    binding.editTextPhone.text.toString(),
                    binding.editTextEmail.text.toString()
                )
                putString(getString(R.string.name), profile.name)
                putString(getString(R.string.phone), profile.phone)
                putString(getString(R.string.email), profile.email)
                apply()
            }

            // Save profile info to Shared Preference
            saveProfilePicture()

            Snackbar.make(this.requireActivity().findViewById(R.id.constraintLayout_second),
                getString(R.string.record_saved), Snackbar.LENGTH_SHORT).show()
        } else if (menuItem.itemId == android.R.id.home) {
            findNavController().navigateUp()
        }
        return true
    }

    private fun saveProfilePicture() {
        val filename = "profile.png"
        val file = File(this.context?.filesDir, filename) // get the app default file path
        val bitmapDrawable = binding.imageViewProfile.drawable as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap // convert a drawable to a bitmap
        val outputStream: OutputStream

        try {
            outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun readProfilePicture() {
        val filename = "profile.png"
        val file = File(context?.filesDir, filename) // get the app default file path
        if (file.exists()) {
            try {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                binding.imageViewProfile.setImageBitmap(bitmap)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }
}