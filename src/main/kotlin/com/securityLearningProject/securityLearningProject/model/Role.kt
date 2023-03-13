package com.securityLearningProject.securityLearningProject.model

enum class Role(permissions: Set<Permission>) {

    ADMIN(hashSetOf(
        Permission.ADMIN_CAR_READ,
        Permission.USER_CAR_READ,
        Permission.USER_CAR_WRITE
    )),
    USER(hashSetOf(
        Permission.USER_CAR_READ
    )),
    MANAGER(hashSetOf(
        Permission.USER_CAR_DELETE,
        Permission.USER_CAR_READ,
        Permission.USER_CAR_WRITE,
        Permission.ADMIN_CAR_READ,
        Permission.ADMIN_CAR_DELETE,
        Permission.ADMIN_CAR_WRITE
    ));

    private var permissions: Set<Permission>? = null

    fun getPermissions(): Set<Permission> {
        return permissions.orEmpty()
    }
}