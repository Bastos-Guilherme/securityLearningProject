package com.securityLearningProject.securityLearningProject.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "Users")
class User: UserDetails {

    companion object {
        private val runtimeException: RuntimeException = RuntimeException()
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null
    @Column(unique = true)
    private var username: String? = null
    @Column(unique = true)
    var email: String? = null
    var name: String? = null
    var lastName: String? = null
    private var password: String? = null
    @ElementCollection(targetClass = Role::class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    var roles: Set<Role> = hashSetOf()
    @ElementCollection(targetClass = Permission::class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    var permissions: Set<Permission> = hashSetOf()
    var isActive: Boolean = true
    var credentialsLastChange: LocalDate = LocalDate.now()
    var lastSigning: LocalDate = LocalDate.now()
    var failedLoginAttempts: Int = 0

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities: MutableSet<GrantedAuthority> = hashSetOf()
        roles.forEach {
            authorities.add(SimpleGrantedAuthority("ROLE_" + it.name))
            it.getPermissions().forEach {
                authorities.add(SimpleGrantedAuthority(it.name))
            }
        }
        permissions.forEach {
            authorities.add(SimpleGrantedAuthority(it.name))
        }
        return authorities
    }

    override fun isAccountNonExpired(): Boolean {
        return lastSigning.plusYears(1) >= LocalDate.now()
    }

    override fun getUsername(): String {
        if (username == null) {
            throw runtimeException
        } else {
            return username!!
        }
    }

    fun setUsername(username: String) {
        this.username = username
    }

    override fun getPassword(): String {
        if (password == null) {
            throw runtimeException
        } else {
            return password!!
        }
    }

    fun setPassword(password: String) {
        this.password = password
    }

    override fun isAccountNonLocked(): Boolean {
        return failedLoginAttempts < 5
    }

    override fun isCredentialsNonExpired(): Boolean {
        return credentialsLastChange.plusYears(1) >= LocalDate.now()
    }

    override fun isEnabled(): Boolean {
        return isActive
    }
}