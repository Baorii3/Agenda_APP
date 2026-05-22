package com.example.agenda.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agenda.R
import com.example.agenda.viewmodel.SalaViewModel
import com.example.agenda.viewmodel.UserViewModel
import com.example.agenda.api.ActivitatRequestDto
import com.example.agenda.dialogs.CreateActivitatDialog
import com.example.agenda.recyclers.ActivitatAdapter
import java.util.*
import android.app.DatePickerDialog

class SalaFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var rvActivitat: RecyclerView
    private lateinit var adapter: ActivitatAdapter

    private val viewModel: SalaViewModel by activityViewModels<SalaViewModel>()
    private val userViewModel: UserViewModel by activityViewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sala, container, false)

        val salaId = arguments?.getLong("salaId") ?: -1L
        if (salaId == -1L) {
            Log.d("SalaFragment", "No se recibió un ID de sala válido")
            return view
        }
        val sala = viewModel.findSalaById(salaId)
        val tvSalaName = view.findViewById<TextView>(R.id.tvSalaTitle)
        val tvSalaDescription = view.findViewById<TextView>(R.id.tvSalaDescription)
        tvSalaName.text = sala?.nom
        tvSalaDescription.text = sala?.descripcio
        viewModel.cargarActivitatsForSala(salaId) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
        adapter = ActivitatAdapter(
            activitats = emptyList(),
            onItemClick = {},
            onItemLongClick = { item, view ->
                val popup = PopupMenu(context, view)
                popup.menuInflater.inflate(R.menu.menu_long_click, popup.menu)
                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.pop_editar -> {
                            val editPopup = com.example.agenda.dialogs.UpdateActivitatDialog.newInstance(
                                item.idActivitat,
                                item.titol,
                                item.descripcio,
                                item.data,
                                item.horaInici,
                                item.horaFi,
                                item.idUsuari
                            )
                            editPopup.show(parentFragmentManager, "UpdateActivitatDialog")
                            true
                        }
                        R.id.pop_eliminar -> {
                            viewModel.deleteActivitats(item.idActivitat) { message ->
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                            }
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
            }
        )
        rvActivitat = view.findViewById(R.id.activitatsRecyclerView)
        rvActivitat.layoutManager = LinearLayoutManager(context)
        rvActivitat.adapter = adapter
        viewModel.activitats.observe(viewLifecycleOwner) { activitats ->
            adapter.updateList(activitats)
        }

        val etFechaFiltro = view.findViewById<EditText>(R.id.etFechaFiltroSala)
        val btnBuscarFecha = view.findViewById<ImageButton>(R.id.btnBuscarFechaSala)

        etFechaFiltro.setOnClickListener {
            val c = Calendar.getInstance()
            val dp = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                val mes = month + 1
                val fecha = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, mes, dayOfMonth)
                etFechaFiltro.setText(fecha)
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
            dp.show()
        }

        btnBuscarFecha.setOnClickListener {
            val fecha = etFechaFiltro.text.toString()
            if (fecha.isNotBlank()) {
                viewModel.buscarActivitatsPorDia(fecha) { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            } else {
                // Si el campo está vacío, recargamos todas las actividades de la sala
                viewModel.cargarActivitatsForSala(salaId) { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Observador de resultados por dia (filtrar por salaId antes de mostrar)
        viewModel.activitatsPorDia.observe(viewLifecycleOwner) { l ->
            val filtradasPorSala = l.filter { it.idSala == salaId }
            adapter.updateList(filtradasPorSala)
        }

        val btnAdd = view.findViewById<ImageView>(R.id.addactivitatButton)
        btnAdd.isVisible = userViewModel.canCreateActividades()
        btnAdd.setOnClickListener {
            if (parentFragmentManager.findFragmentByTag("CreateActivitatDialog") == null) {
                CreateActivitatDialog().show(parentFragmentManager, "CreateActivitatDialog")
            }
        }

        userViewModel.user.observe(viewLifecycleOwner) { _ ->
            btnAdd.isVisible = userViewModel.canCreateActividades()
        }

        parentFragmentManager.setFragmentResultListener("createActivitatRequest", this) { _, bundle ->
            val titol = bundle.getString("titol")
            val descripcio = bundle.getString("descripcio")
            val data = bundle.getString("data")
            val horaInici = bundle.getString("horaInici")
            val horaFi = bundle.getString("horaFi")
            val professorId = bundle.getLong("usuariId")
            val actRequest = ActivitatRequestDto(
                idSala = salaId,
                idUsuari = professorId,
                titol = titol ?: "",
                descripcio = descripcio ?: "",
                data = data ?: "",
                horaInici = horaInici ?: "",
                horaFi = horaFi ?: ""
            )
            viewModel.crearActivitat(actRequest) { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }

        parentFragmentManager.setFragmentResultListener("updateActivitatRequest", this) { _, bundle ->
            val idActivitat = bundle.getLong("idActivitat")
            val titol = bundle.getString("titol")
            val descripcio = bundle.getString("descripcio")
            val data = bundle.getString("data")
            val horaInici = bundle.getString("horaInici")
            val horaFi = bundle.getString("horaFi")
            val usuariId = bundle.getLong("usuariId")

            if (idActivitat != -1L) {
                val actRequest = ActivitatRequestDto(
                    idSala = salaId,
                    idUsuari = usuariId,
                    titol = titol ?: "",
                    descripcio = descripcio ?: "",
                    data = data ?: "",
                    horaInici = horaInici ?: "",
                    horaFi = horaFi ?: ""
                )
                viewModel.updateActivitat(idActivitat, actRequest) { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.e("SalaFragment", "updateActivitatRequest recibido con id inválido")
            }
        }

        return view


    }
}