import { AuthenticationService } from './../../services/authentication/authentication.service';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationRequest } from 'src/app/models/authentication-request';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  userlogin: AuthenticationRequest = {};
  errMsg = '';

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ) { }

  login() {
    this.errMsg = '';
    this.authenticationService.login(this.userlogin)
      .subscribe({
        next: (authenticationResponse) => {
          localStorage.setItem('user', JSON.stringify(authenticationResponse));
          this.router.navigate(['customers']);
        },
        error: (err) => {
          if (err.error.statusCode === 401) {
            this.errMsg = 'Login and / or password is incorrect';
          }
        }
      });
  }
  register() {
    this.router.navigate(['register']);
  }
}
