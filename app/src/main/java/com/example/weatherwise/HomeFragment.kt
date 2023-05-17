package com.example.weatherwise

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.weatherwise.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    private var searchView: SearchView? = null

    private val weatherViewModel : WeatherViewModel by viewModels()

    private val binding
        get() = checkNotNull(_binding) {
        "Binding is null"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost : MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_home, menu)

                val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
                searchView = searchItem.actionView as? SearchView

                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        Log.d(TAG, "QueryTextSubmit: $query")
                        viewLifecycleOwner.lifecycleScope.launch {
                            if (!weatherViewModel.getWeather(query)) {
                                Toast.makeText(context, "Invalid: check to make sure you have entered a valid zipcode", Toast.LENGTH_SHORT).show()
                            }
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        Log.d(TAG, "QueryTextChange: $newText")
                        return false
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_item_clear -> {
                        Log.d("TAG", "cleared")
                        true
                    }
                    else -> false
                }
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            weatherViewModel.weatherResponse.collect {
                binding.planningTv.text = getString(R.string.planning_the_day, it.zipcode)
                binding.descTv.text = it.description
                if (it.days.isNotEmpty()) {
                    binding.tempTv.text = getString(R.string.temperature, it.days[0].temp.toInt())
                    binding.conditionsTv.text = it.days[0].conditions
                    binding.precipitationTv.text = getString(R.string.precip_percent, it.days[0].precipprob.toInt())
                    binding.humidityTv.text = getString(R.string.humid_percent, it.days[0].humidity.toInt())
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            weatherViewModel.placesResponse.collect {
                if (it.results.isNotEmpty()) {
                    binding.nameTv.text = it.results[0].name
                    binding.addressTv.text = it.results[0].address
                    Log.d(TAG, "yay got a new emit!")
                }
            }
        }

        // TODO: Add dialogues for each of the weather attributes

        binding.parksIv.setOnClickListener {
            binding.museumsIv.background = null
            binding.restaurantsIv.background = null
            binding.parksIv.background = getDrawable(this.requireContext(), R.drawable.rounded_corner_button)
            viewLifecycleOwner.lifecycleScope.launch {
                weatherViewModel.clickButton(0)
            }
        }

        binding.museumsIv.setOnClickListener {
            binding.museumsIv.background = getDrawable(this.requireContext(), R.drawable.rounded_corner_button)
            binding.restaurantsIv.background = null
            binding.parksIv.background = null
            viewLifecycleOwner.lifecycleScope.launch {
                weatherViewModel.clickButton(1)
            }
        }

        binding.restaurantsIv.setOnClickListener {
            binding.museumsIv.background = null
            binding.restaurantsIv.background = getDrawable(this.requireContext(), R.drawable.rounded_corner_button)
            binding.parksIv.background = null
            viewLifecycleOwner.lifecycleScope.launch {
                weatherViewModel.clickButton(2)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}