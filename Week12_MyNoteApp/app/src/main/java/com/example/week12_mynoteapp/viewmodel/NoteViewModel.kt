package com.example.week12_mynoteapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.week12_mynoteapp.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel untuk mengelola data catatan secara in-memory menggunakan StateFlow.
 */
class NoteViewModel : ViewModel() {

    // Backing property untuk StateFlow daftar catatan
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    /**
     * Menyimpan atau memperbarui catatan.
     */
    fun saveNote(id: Long?, content: String) {
        if (content.isBlank()) return

        _notes.update { currentNotes ->
            if (id == null) {
                // Tambahkan catatan baru di posisi teratas
                val newNote = Note(
                    id = System.currentTimeMillis(),
                    content = content
                )
                listOf(newNote) + currentNotes
            } else {
                // Perbarui catatan yang sudah ada
                currentNotes.map { note ->
                    if (note.id == id) {
                        note.copy(content = content, updatedAt = System.currentTimeMillis())
                    } else {
                        note
                    }
                }.sortedByDescending { it.updatedAt } // Urutkan berdasarkan waktu terbaru
            }
        }
    }

    /**
     * Menghapus catatan berdasarkan ID.
     */
    fun deleteNote(id: Long) {
        _notes.update { currentNotes ->
            currentNotes.filterNot { it.id == id }
        }
    }

    /**
     * Mengambil catatan berdasarkan ID.
     */
    fun getNoteById(id: Long): Note? {
        return _notes.value.find { it.id == id }
    }
}
