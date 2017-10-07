import {Component, Input, OnDestroy} from "@angular/core";
import {Message} from "primeng/primeng";
import {ActivatedRoute, Router} from "@angular/router";
import {AppUser} from "./user";
import {AppUserDataService} from "./user.data.service";
import {Role} from "../role/role";
import {RoleDataService} from "../role/role.data.service";
import {Report} from "../report/report";

@Component({
  selector: 'users-add',
  templateUrl: 'user.add.component.html',
})
export class AppUsersAddComponent implements OnDestroy {

  @Input() header = "Sprawozdania użytkownika...";
  user : AppUser;
  userReports: Report[] = [];

  private params_subscription: any;

  sourceRoles : Role[] = [];

  msgs: Message[] = [];

  constructor(private route: ActivatedRoute, private router: Router, private userService: AppUserDataService, private roleService : RoleDataService) {

    this.params_subscription = this.route.params.subscribe(params => {
      let id = params['id'];
      console.log('Constructor for users-add ' + id);
      if (id === 'add') {
        this.user = new AppUser();

        roleService.findAllRolesWhichDoNotHaveAppUserWithThisId(0).
        subscribe(roles => this.sourceRoles = roles,
          error => this.msgs.push({severity:'error', summary:'Constructor user roles error', detail: error}))

      } else {
        this.userService.getUser(id)
          .subscribe(user => {
              this.user = user;
              this.userReports = user.reports;

              roleService.findAllRolesWhichDoNotHaveAppUserWithThisId(this.user.id).
              subscribe(roles => this.sourceRoles = roles,
                error => this.msgs.push({severity:'error', summary:'Constructor user roles error', detail: error}))

            },
            error => this.msgs.push({severity:'error', summary:'Constructor error', detail: error})
          );
      }
    });
  }

  ngOnDestroy() {
      this.params_subscription.unsubscribe();
  }

  onSave() {
    this.userService.update(this.user).
    subscribe(
      user => {
        this.user = user;
        this.msgs = []; //this line fix disappearing of messages
        this.msgs.push({severity:'info', summary:'Zapisano', detail: 'OK!'})
      },
      error => this.msgs.push({severity:'error', summary:'Nie można zapisać', detail: error})
    );
  }

  onRowSelect(event : any) {
    let id =  event.data.id;
    this.router.navigate(['/reports', id]);
  }

  changePassword: boolean = false;

  wantChangePassword(){
    this.changePassword = true;
  }

  oldPassword: string;
  newPassword: string;
  newPasswordRepeat: string;

  onChangePassword(){
    this.msgs = []; //this line fix disappearing of messages

    this.userService.changePassword(this.user.id, this.oldPassword, this.newPassword, this.newPasswordRepeat).
    subscribe(
      user => {
        this.user = user;
        this.msgs = []; //this line fix disappearing of messages
        this.msgs.push({severity:'info', summary:'Zmieniono hasło', detail: 'OK!'})
      },
      error => this.msgs.push({severity:'error', summary:'Nie można zmienić hasła', detail: error})
    );
  }
}
