import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { CustomerDTO } from 'src/app/models/customer-dto';

@Component({
  selector: 'app-header-bar',
  templateUrl: './header-bar.component.html',
  styleUrls: ['./header-bar.component.scss']
})
export class HeaderBarComponent implements OnInit {
  constructor(private router: Router) { }

  user: CustomerDTO = {};
  ngOnInit(): void {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      this.user = JSON.parse(storedUser).customerDTO;
    }
  }

  items: Array<MenuItem> = [
    {
      label: "Profile",
      icon: 'pi pi-user'
    },
    {
      label: "Settings",
      icon: 'pi pi-cog'
    },
    {
      separator: true
    },
    {
      label: "Sign out",
      icon: 'pi pi-sign-out ',
      command: () => {
        localStorage.clear();
        this.router.navigate(['login']);
      }
    },
  ];

  get username(): string {
    return this.user.username;
  }
  get userRole(): string {
    return this.user.roles[0];
  }
}
