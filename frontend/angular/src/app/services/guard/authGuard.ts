import { AuthenticationResponse } from 'src/app/models/authentication-response';
import { inject } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { Observable } from "rxjs";
import { JwtHelperService } from '@auth0/angular-jwt';

export const AuthGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
):
  Observable<boolean | UrlTree>
  | Promise<boolean | UrlTree>
  | boolean
  | UrlTree => {
  const storedUser = localStorage.getItem('user');
  if (storedUser) {
    const authResponse: AuthenticationResponse = JSON.parse(storedUser);
    const token = authResponse.token;
    if (token) {
      const jwtHelper = new JwtHelperService();
      const isTokenNotExpired = !jwtHelper.isTokenExpired(token);
      if (isTokenNotExpired) {
        return true;
      }
    }
  }

  return inject(Router).createUrlTree(['login']);



};
