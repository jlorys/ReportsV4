import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {MenuItem} from 'primeng/primeng';
import {AuthService} from '../../service/auth.service';
import {Message} from 'primeng/primeng';
import {AppUserDataService} from "../component.admin/users/user.data.service";
import {AppUser} from "../component.admin/users/user";
import {Router} from "@angular/router";
import {forEach} from "@angular/router/src/utils/collection";
import {Role} from "../component.admin/role/role";

@Component({
  selector: 'app-main',
  templateUrl: './app.component.html',
  styles: [`
        .layout div {
            background-color: white;
            border: 1px solid #f5f7f8;
        }
    `]
})
export class AppComponent implements OnInit {
  private items: MenuItem[];

  msgs: Message[] = [];

  displayLoginDialog: boolean = false;
  displayRegistrationDialog: boolean = false;
  loginFailed: boolean = false;
  authenticated: boolean = false;

  username: string = "admin";
  password: string = "admin";

  user : AppUser;
  loggedUser: AppUser = new AppUser;
  loggedUserRoles: Role[] = [];

  constructor(private authService: AuthService, private appUserDataService: AppUserDataService) {
    this.user = new AppUser();
  }

  ngOnInit() {
    this.items = [
      {label: 'Strona główna', routerLink: ['/'], icon: 'fa-home'},

      {
        label: 'Wykresy',
        icon: 'fa-bar-chart',
        items: [
          {label: 'Wykres kołowy', icon: 'fa-pie-chart', routerLink: ['/doughnut']},
          {label: 'Diagram czasu', icon: 'fa-calendar-check-o', routerLink: ['/schedule']},
        ]
      },

      {
        label: 'Dokumentacja',
        icon: 'fa-book',
        items: [
          {label: "Angular 4", icon: 'fa-location-arrow', url: "https://angular.io/"},
          {label: "TypeScript", icon: 'fa-location-arrow', url: "https://www.typescriptlang.org/"},
          {label: "Spring Security", icon: 'fa-envira', url: "https://docs.spring.io/spring-security/"},
          {label: "Spring Boot", icon: 'fa-envira', url: "http://projects.spring.io/spring-boot/"},
          {label: "Spring Data JPA", icon: 'fa-envira', url: "http://projects.spring.io/spring-data-jpa/"},
          {label: "Angular Cli", icon: 'fa-location-arrow', url: "https://cli.angular.io/"},
          {label: "Material 2", icon: 'fa-location-arrow', url: "https://material.angular.io/"},
          {label: "PrimeNG", icon: 'fa-location-arrow', url: "http://www.primefaces.org/primeng"}
        ]
      }
    ];

    this.authService.isAuthenticated().subscribe(
      resp => {
        this.authenticated = resp;
        this.displayLoginDialog = !this.authenticated;
        this.msgs = []; //this line fix disappearing of messages
        if (this.authenticated) {
          this.addProperRoleItems();
          this.items.unshift({label: 'Wyloguj się', url: '/api/logout', icon: 'fa-long-arrow-left'});
          console.log('You are authenticated...', '');
        } else {
          console.log('You are NOT authenticated...', '');
        }
      },
      error => this.msgs.push({severity: 'error', summary: 'isAuthenticated Error', detail: error})
    );
  }

  login() {
    this.authService.login(this.username, this.password).subscribe(
      loginOk => {
        if (loginOk) {
          this.displayLoginDialog = false;
          this.authenticated = true;
          this.addProperRoleItems();
          this.items.unshift({label: 'Wyloguj się', url: '/api/logout', icon: 'fa-long-arrow-left'});
          this.loginFailed = false;
          this.msgs = []; //this line fix disappearing of messages
        } else {
          this.loginFailed = true;
          this.displayLoginDialog = true;
          this.authenticated = false;
        }
      },
      error => {
        this.msgs = []; //this line fix disappearing of messages
        this.msgs.push({severity: 'error', summary: 'Login error', detail: error});
        this.loginFailed = true;
        this.displayLoginDialog = true;
        this.authenticated = false;
      }
    );
  }

  register() {
    this.msgs = []; //this line fix disappearing of messages

    if(!this.user.userName){
      this.msgs.push({severity: 'error', summary: 'Register error', detail: "Nie ma username!"});
    }
    else if(!this.user.firstName){
      this.msgs.push({severity: 'error', summary: 'Register error', detail: "Nie ma imienia!"});
    }
    else if(!this.user.lastName){
      this.msgs.push({severity: 'error', summary: 'Register error', detail: "Nie ma nazwiska!"});
    }
    else if(!this.user.email){
      this.msgs.push({severity: 'error', summary: 'Register error', detail: "Nie ma emaila!"});
    }
    else if(!this.user.password){
      this.msgs.push({severity: 'error', summary: 'Register error', detail: "Nie ma hasła!"});
    }
    else{

      this.appUserDataService.register(this.user).subscribe(
        user => {
          this.msgs.push({severity:'info', summary:'Zarejestrowano', detail: 'OK!'})
        },
        error => this.msgs.push({severity:'error', summary:'Nie można zarejestrować', detail: error})
      );

    }
  }

  dialogLogin() {
    this.displayLoginDialog = true;
    this.displayRegistrationDialog = false;
  }

  dialogRegister() {
    this.displayLoginDialog = false;
    this.displayRegistrationDialog = true;
  }

  showUserInfo() {
    this.msgs = []; //this line fix disappearing of messages
    this.appUserDataService.getLoggedUser().subscribe(
      pageResponse => {
        this.loggedUser = pageResponse;
        this.loggedUserRoles = this.loggedUser.roles;

        this.msgs.push({
          severity: 'info', summary: 'Zalogowany użytkownik: ', detail:
          '<p><b>username:</b> ' + this.loggedUser.userName +
          '</p><p><b>imię:</b> ' + this.loggedUser.firstName +
          '</p><p><b>nazwisko:</b> ' + this.loggedUser.lastName
        });
      },
      error => this.msgs.push({severity: 'error', summary: 'Nie dało się otrzymać wyników', detail: error})
    )
  }

  addProperRoleItems() {
    this.authService.isLoggedUserHasRoleUser().subscribe(
      response => {
        if (response) {
          this.pushUserItem()
        }

        this.authService.isLoggedUserHasRoleReviewer().subscribe(
          response => {
            if (response) {
              this.pushReviewerItem()
            }

            this.authService.isLoggedUserHasRoleAdmin().subscribe(
              response => {
                if (response) {
                  this.pushAdminItem()
                }
                this.showUserInfo();
              }
            );
          }
        );
      }
    );
  }

  pushAdminItem() {
    this.items.push(
      {
        label: 'Admin',
        icon: 'fa-fort-awesome',
        items: [
          {
            label: 'Sprawozdania',
            icon: 'fa-file-text-o',
            items: [
              {label: 'Wyszukiwanie Sprawozdania', icon: 'fa-search', routerLink: ['/reports']},
            ]
          },

          {
            label: 'Użytkownicy',
            icon: 'fa-user-circle',
            items: [
              {label: 'Wyszukiwanie Użytkownika', icon: 'fa-search', routerLink: ['/users']},
              {label: 'Tworzenie Użytkownika', icon: 'fa-gavel', routerLink: ['/users/add']},
            ]
          },

          {
            label: 'Role', icon: 'fa-universal-access', routerLink: ['/roles'],
          },

          {
            label: 'Laboratoria',
            icon: 'fa-columns',
            items: [
              {label: 'Wyszukiwanie Laboratoriów', icon: 'fa-search', routerLink: ['/laboratories']},
              {label: 'Tworzenie Laboratoriów', icon: 'fa-gavel', routerLink: ['/laboratories/add']},
            ]
          },

          {
            label: 'Przemioty',
            icon: 'fa-square',
            items: [
              {label: 'Wyszukiwanie Przemiotów', icon: 'fa-search', routerLink: ['/subjects']},
              {label: 'Tworzenie Przedmiotów', icon: 'fa-gavel', routerLink: ['/subjects/add']},
            ]
          },

          {
            label: 'Kierunki studiów',
            icon: 'fa-arrows',
            items: [
              {label: 'Wyszukiwanie kierunków', icon: 'fa-search', routerLink: ['/fieldsOfStudies']},
              {label: 'Tworzenie kierunków', icon: 'fa-gavel', routerLink: ['/fieldsOfStudies/add']},
            ]
          },
        ]
      }
    );
  }

  pushReviewerItem() {
    this.items.push(
      {
        label: 'Recenzent',
        icon: 'fa-balance-scale',
        items: [
          {
            label: 'Oceń Sprawozdania', icon: 'fa-file-text-o', routerLink: ['/reviewerReports'],
          },

          {
            label: 'Moje konto', icon: 'fa-search', routerLink: ['/userAccount'],
          },

          {
            label: 'Role', icon: 'fa-universal-access', routerLink: ['/roles'],
          },

          {
            label: 'Laboratoria',
            icon: 'fa-columns',
            items: [
              {label: 'Wyszukiwanie Laboratoriów', icon: 'fa-search', routerLink: ['/reviewerLaboratories']},
              {label: 'Tworzenie Laboratoriów', icon: 'fa-gavel', routerLink: ['/reviewerLaboratories/add']},
            ]
          },

          {
            label: 'Przemioty',
            icon: 'fa-square',
            items: [
              {label: 'Wyszukiwanie Przemiotów', icon: 'fa-search', routerLink: ['/reviewerSubjects']},
              {label: 'Tworzenie Przedmiotów', icon: 'fa-gavel', routerLink: ['/reviewerSubjects/add']},
            ]
          },

          {
            label: 'Kierunki studiów',
            icon: 'fa-arrows',
            items: [
              {label: 'Wyszukiwanie kierunków', icon: 'fa-search', routerLink: ['/reviewerFieldsOfStudies']},
              {label: 'Tworzenie kierunków', icon: 'fa-gavel', routerLink: ['/reviewerFieldsOfStudies/add']},
            ]
          },
        ]
      }
    );
  }

  pushUserItem() {
    this.items.push(
      {
        label: 'Użytkownik',
        icon: 'fa-eercast',
        items: [
          {
            label: 'Moje Sprawozdania', icon: 'fa-file-text-o', routerLink: ['/userReports'],
          },

          {
            label: 'Tworzenie Sprawozdania', icon: 'fa-gavel', routerLink: ['/userReports/add'],
          },

          {
            label: 'Moje konto', icon: 'fa-search', routerLink: ['/userAccount'],
          },

          {
            label: 'Role', icon: 'fa-universal-access', routerLink: ['/roles'],
          },

          {
            label: 'Laboratoria', icon: 'fa-columns', routerLink: ['/userLaboratories'],
          },

          {
            label: 'Przemioty', icon: 'fa-square', routerLink: ['/userSubjects'],
          },

          {
            label: 'Kierunki studiów', icon: 'fa-arrows', routerLink: ['/userFieldsOfStudies'],
          },
        ]
      }
    );
  }

  // sample method from angular doc
  private handleError(error: any) {
    let errMsg = (error.message) ? error.message :
      error.status ? `Status: ${error.status} - Text: ${error.statusText}` : 'Server error';
    console.error(errMsg); // log to console instead
    return Observable.throw(errMsg);
  }
}
