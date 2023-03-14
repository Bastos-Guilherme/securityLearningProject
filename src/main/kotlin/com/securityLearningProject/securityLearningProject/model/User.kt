package com.securityLearningProject.securityLearningProject.model

import com.securityLearningProject.securityLearningProject.service.`interface`.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "Users")
class User: UserDetails {

    @Autowired
    private lateinit var authService: AuthService

    companion object {
        private val runtimeException = RuntimeException()
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
    var credentialsLastChange = LocalDate.now()
    var lastSigning = LocalDate.now()
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

    override fun isAccountNonExpired(): Boolean {
        return !authService.isAccountExpired(this)
    }

    override fun isAccountNonLocked(): Boolean {
        return !authService.isAccountLocked(this)
    }

    override fun isCredentialsNonExpired(): Boolean {
        return !authService.isCredentialsExpired(this)
    }

    override fun isEnabled(): Boolean {
        return authService.isActive(this)
    }
}