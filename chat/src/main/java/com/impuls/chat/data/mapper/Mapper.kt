package com.impuls.chat.data.mapper

import com.impuls.chat.data.remote.response.GroupDto
import com.impuls.chat.data.remote.response.create_chat_dto.ChatDto
import com.impuls.chat.data.remote.response.create_chat_dto.ConnectionDto
import com.impuls.chat.data.remote.response.create_chat_dto.MessageDto
import com.impuls.chat.data.remote.response.create_chat_dto.RoleDto
import com.impuls.chat.data.remote.response.create_chat_dto.UserDto
import com.impuls.chat.domain.model.Chat
import com.impuls.chat.domain.model.Connection
import com.impuls.chat.domain.model.Message
import com.impuls.chat.domain.model.Role
import com.impuls.chat.domain.model.StatusMessage
import com.impuls.chat.domain.model.SubGroup
import com.impuls.chat.domain.model.User
import com.impuls.chat.domain.utils.Utils

fun GroupDto.convertToSubGroups(): List<SubGroup> {
    return this.subgroups.map {
        SubGroup(
            id = it.id,
            name = it.name
        )
    }
}

fun RoleDto.convertToRole(): Role {
    return Role(
        id = this.id,
        name = this.name
    )
}

fun UserDto.convertToUser(): User {
    return User(
        id = this.id,
        name = this.name,
        email = this.email,
        isDisabled = this.isDisabled,
        role = this.role.convertToRole()
    )
}

fun MessageDto.convertToMessage(): Message {
    return Message(
        date = Utils.convertToLocalDateTime(this.date),
        id = this.id,
        isSupport = this.isSupport,
        text = this.text,
        statusMessage = StatusMessage.DELIVERED
    )
}

fun ConnectionDto.convertToConnection(): Connection {
    return Connection(
        end = if (this.end != null) Utils.convertToLocalDateTime(this.end) else null,
        id = this.id,
        messages = this.messages?.map { it.convertToMessage() } ?: emptyList(),
        start = Utils.convertToLocalDateTime(
            this.start
        ),
        user = this.user.convertToUser()
    )
}

fun ChatDto.convertToChat(): Chat {
    return Chat(
        connections = this.connections.map { it.convertToConnection() },
        createdDate = Utils.convertToLocalDateTime(this.createdDate),
        email = this.email,
        id = this.id,
        isOpen = this.isOpen,
        isWork = this.isWork,
        name = this.name,
        phone = this.phone,
        subgroup = SubGroup(
            id = this.subgroup.id,
            name = this.subgroup.name
        )
    )
}

