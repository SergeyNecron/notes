package ru.notes.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.notes.dto.NoteDtoIn
import ru.notes.dto.NoteDtoOut
import ru.notes.exception.NotFoundException
import ru.notes.model.Note
import ru.notes.repository.NoteRepository

/**
 *
 * @author Sergey Muratkin
 * Date: 2020-04-18
 */
@Service
class NoteServiceImpl
@Autowired constructor(
        private val noteRepository: NoteRepository
) : NoteService {

    companion object {
        val log: Logger = LoggerFactory.getLogger(NoteServiceImpl::class.java)!!
    }

    @Transactional(readOnly = true)
    override fun getAllNotes(): List<NoteDtoOut>? =
            noteRepository
                    .findAll()
                    .map { NoteDtoOut(it) }
                    .sortedBy { it.id }


    @Transactional(readOnly = true)
    override fun getNote(id: Long): NoteDtoOut =
            NoteDtoOut(getNoteById(id))

    @Transactional
    override fun addNote(noteDtoIn: NoteDtoIn): NoteDtoOut =
            NoteDtoOut(noteRepository.save(noteDtoIn.convertToNote()))


    @Transactional
    override fun updateNote(id: Long, noteDtoOut: NoteDtoOut): NoteDtoOut =
            NoteDtoOut(noteRepository.save(getNoteById(id).updateFromDto(noteDtoOut)))

    @Transactional
    override fun deleteNote(id: Long): Boolean {
        noteRepository.delete(getNoteById(id))
        return true
    }

    override fun findNotes(text: String): List<NoteDtoOut>? {
        return if (text.isEmpty())
            getAllNotes() else
            noteRepository.findByName(text)
                    ?.map { NoteDtoOut(it) }
                    ?.sortedBy { it.id }
    }

    private fun getNoteById(id: Long): Note =
            noteRepository
                    .findById(id)
                    .orElseThrow {
                        log.error("Note with id: $id not found")
                        NotFoundException(id)
                    }
}